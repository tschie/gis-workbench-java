package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.view;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.model.SelectEvent;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.model.WebMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.model.WebMaps;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.xml.XmlFileSerializer;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.collections.ListChangeListener;
import javafx.scene.control.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class WebMapsView extends TreeView<ArcGISMap> {

    private final Workspace workspace;
    private final EventService eventService;
    private final XmlFileSerializer xmlFileSerializer = new XmlFileSerializer();

    public WebMapsView(Workspace workspace, EventService eventService) {
        this.workspace = workspace;
        this.eventService = eventService;

        setShowRoot(false);
        TreeItem<ArcGISMap> root = new TreeItem<>();
        root.setExpanded(true);
        setRoot(root);

        WebMaps webMaps = deserialize();
        if (webMaps != null) {
            root.getChildren().addAll(webMaps.getWebMaps().stream()
                    .map(w -> new PortalItem(new Portal(w.getUrl()), w.getId()))
                    .map(ArcGISMap::new)
                    .map(TreeItem::new)
                    .collect(Collectors.toList())
            );
            // load and refresh all
            root.getChildren().forEach(item -> {
                ArcGISMap map = item.getValue();
                map.loadAsync();
                map.addDoneLoadingListener(() ->
                        root.getChildren().replaceAll(i -> i.equals(item) ? item : i)
                );
            });
        }
        root.getChildren().addListener((ListChangeListener<TreeItem<ArcGISMap>>) change -> {
            while (change.next()) {
                serialize();
            }
        });

        setCellFactory((c) -> new WebMapTreeCell(workspace, eventService));

        ContextMenu contextMenu = new ContextMenu();
        setContextMenu(contextMenu);

        Menu newMenu = new Menu("New");
        contextMenu.getItems().add(newMenu);

        MenuItem webMapMenuItem = new MenuItem("Web map");
        newMenu.getItems().add(webMapMenuItem);
        webMapMenuItem.setOnAction(e -> {
            TextInputDialog webMapUrlInputDialog = new TextInputDialog();
            webMapUrlInputDialog.setTitle("New web map");
            webMapUrlInputDialog.setContentText("URL:");
            webMapUrlInputDialog.getEditor().setPromptText("http://www.arcgis.com/home/item.html?id=[web_map_id]");
            webMapUrlInputDialog.showAndWait().ifPresent(url -> {
                PortalItem portalItem = new PortalItem(new Portal("http://www.arcgis.com"), url);
                ArcGISMap map = new ArcGISMap(portalItem);
                TreeItem<ArcGISMap> item = new TreeItem<>(map);
                map.loadAsync();
                // refresh item after load
                map.addDoneLoadingListener(() ->
                        root.getChildren().replaceAll(i -> i.equals(item) ? item : i)
                );
                root.getChildren().add(item);
            });
        });

        getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                eventService.emit(new SelectEvent(newValue.getValue(), workspace));
            }
        }));
    }

    private File getWebMapsXmlFile() {
        return workspace.getMetadataDirectory().toPath().resolve(getClass().getPackageName() + ".xml").toFile();
    }

    private WebMaps deserialize() {
        File windowXmlFile = getWebMapsXmlFile();
        if (windowXmlFile.exists()) {
            return xmlFileSerializer.deserialize(windowXmlFile, WebMaps.class);
        }
        return null;
    }

    private void serialize() {
        File xmlFile = getWebMapsXmlFile();
        // create file if it does not exist
        if (!xmlFile.exists()) {
            try {
                if (!xmlFile.getParentFile().exists()) {
                    Files.createDirectory(xmlFile.getParentFile().toPath());
                }
                Files.createFile(xmlFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // serialize state to file
        if (xmlFile.exists()) {
            WebMaps webMaps = new WebMaps(getRoot().getChildren().stream()
                    .map(TreeItem::getValue)
                    .filter(m -> m.getItem() instanceof PortalItem)
                    .map(m -> new WebMap(((PortalItem) m.getItem()).getPortal().getUri(), m.getItem().getItemId()))
                    .collect(Collectors.toList())
            );
            xmlFileSerializer.serialize(getWebMapsXmlFile(), webMaps);
        }
    }
}
