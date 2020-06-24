package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.view;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.Activator;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.model.Layout;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.modules.WorkbenchModule;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.Perspective;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.PerspectiveProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.PerspectiveService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.xml.XmlFileSerializer;
import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import com.google.inject.Module;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.osgi.framework.FrameworkUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Workbench extends Stage {

    @Inject
    private FXMLLoader fxmlLoader;

    private final XmlFileSerializer xmlFileSerializer = new XmlFileSerializer();

    private final Workspace workspace;

    private final ObservableList<Perspective> customPerspectives = FXCollections.observableArrayList();

    private final ObjectProperty<Perspective> perspective = new SimpleObjectProperty<>(null);

    public Workbench(Workspace workspace, PerspectiveService perspectiveService, List<Module> serviceModules) {
        super();
        this.workspace = workspace;

        // injects an fxmlLoader containing the following modules
        List<Module> modules = new ArrayList<>(serviceModules);
        modules.add(new WorkbenchModule(this));
        GuiceContext guiceContext = new GuiceContext(this, () -> modules);
        guiceContext.init();

        // initialize scene from FXML
        fxmlLoader.setClassLoader(Activator.class.getClassLoader());
        fxmlLoader.setLocation(FrameworkUtil.getBundle(Activator.class).getResource("/fxml/workbench.fxml"));
        try {
            Parent root = fxmlLoader.load();
            setScene(new Scene(root));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // add stage icon(s)
        List<Image> icons = Stream.of(
                "/assets/arcgis-open-source-apps-32.png",
                "/assets/arcgis-open-source-apps-64.png",
                "/assets/arcgis-open-source-apps-128.png",
                "/assets/arcgis-open-source-apps-256.png",
                "/assets/arcgis-open-source-apps-512.png"
        ).map(path -> {
            try {
                return new Image(FrameworkUtil.getBundle(Activator.class).getResource(path).openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        getIcons().addAll(icons);

        // deserialize layout or use default
        if (workspace != null) {
            Layout initialLayout = deserializeLayout();
            if (initialLayout == null) {
                initialLayout = new Layout();
            }

            // apply layout
            Layout layout = initialLayout;
            setWidth(layout.getWidth());
            setHeight(layout.getHeight());
            setX(layout.getX());
            setY(layout.getY());
            setMaximized(layout.getMaximized());
            customPerspectives.addAll(layout.getPerspectives());
            List<Perspective> perspectives = new ArrayList<>();
            // search provider presets and custom perspectives
            perspectives.addAll(perspectiveService.getAll().stream().map(PerspectiveProviderService::getPerspective).collect(Collectors.toList()));
            perspectives.addAll(customPerspectives);
            // set matching perspective
            perspectives.stream()
                .filter(p -> p.getName().equals(layout.getPerspectiveName()))
                .findFirst()
                .ifPresent(this.perspective::setValue);
            perspectiveService.observableListProperty().addListener((ListChangeListener<PerspectiveProviderService>) change -> {
                while (change.next()) {
                    change.getAddedSubList().stream()
                        .map(PerspectiveProviderService::getPerspective)
                        .filter(p -> p.getName().equals(layout.getPerspectiveName()))
                        .findFirst()
                        .ifPresent(this.perspective::setValue);
                }
            });

            // watch layout for changes
            xProperty().addListener(this::serializeLayout);
            yProperty().addListener(this::serializeLayout);
            widthProperty().addListener(this::serializeLayout);
            heightProperty().addListener(this::serializeLayout);
            maximizedProperty().addListener(this::serializeLayout);
            customPerspectives.addListener((ListChangeListener<Perspective>) change -> {
                while (change.next()) {
                    serializeLayout();
                }
            });
            perspective.addListener(this::serializeLayout);
        }
    }

    public ObservableList<Perspective> getCustomPerspectives() {
        return customPerspectives;
    }

    public Perspective getPerspective() {
        return perspective.get();
    }

    public ObjectProperty<Perspective> perspectiveProperty() {
        return perspective;
    }

    public void setPerspective(Perspective perspective) {
        this.perspective.set(perspective);
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    private File getLayoutXmlFile() {
        return workspace.getMetadataDirectory().toPath().resolve("layout.xml").toFile();
    }

    private Layout deserializeLayout() {
        File windowXmlFile = getLayoutXmlFile();
        if (windowXmlFile.exists()) {
            return xmlFileSerializer.deserialize(windowXmlFile, Layout.class);
        }
        return null;
    }

    private void serializeLayout(Observable observable) {
        serializeLayout();
    }

    private void serializeLayout() {
        File windowXmlFile = getLayoutXmlFile();
        // create file if it does not exist
        if (!windowXmlFile.exists()) {
            try {
                if (!windowXmlFile.getParentFile().exists()) {
                    Files.createDirectory(windowXmlFile.getParentFile().toPath());
                }
                Files.createFile(windowXmlFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // serialize state to file
        if (windowXmlFile.exists()) {
            Layout layout = new Layout(getX(), getY(), getWidth(), getHeight(), isMaximized(), customPerspectives,
                    getPerspective() == null ? "Empty" : getPerspective().getName());
            xmlFileSerializer.serialize(getLayoutXmlFile(), layout);
        }
    }

}
