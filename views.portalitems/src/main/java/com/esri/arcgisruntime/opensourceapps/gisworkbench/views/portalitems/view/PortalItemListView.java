package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.portalitems.view;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.service.PortalItemComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.workspace.service.WorkspacePortalItemComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.events.editor.SelectEditorEvent;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.portalitems.model.PortalItemEntity;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.UUID;
import java.util.stream.Collectors;

public class PortalItemListView extends ListView<PortalItemEntity> {

    private final ObservableMap<UUID, PortalItem> portalItems;
    private final EventService eventService;
    private final Workspace workspace;

    public PortalItemListView(
        Workspace workspace,
        EventService eventService,
        PortalItemComponentService portalItemComponentService,
        WorkspacePortalItemComponentService workspacePortalComponentService
    ) {

        this.workspace = workspace;
        this.eventService = eventService;
        setCellFactory((c) -> new PortalItemListCell(workspace, eventService));

        ContextMenu contextMenu = new ContextMenu();
        setContextMenu(contextMenu);

        Menu newMenu = new Menu("New");
        contextMenu.getItems().add(newMenu);

        MenuItem newPortalMenuItem = new MenuItem("Portal");
        newMenu.getItems().add(newPortalMenuItem);

        newPortalMenuItem.setOnAction(e -> {
            NewPortalItemDialog newPortalItemDialog = new NewPortalItemDialog();
            newPortalItemDialog.showAndWait().ifPresent(portalItem ->
                workspacePortalComponentService.get(workspace).put(UUID.randomUUID(), portalItem)
            );
        });

        portalItems = portalItemComponentService.get(workspace);
        getItems().addAll(portalItems.entrySet().stream()
            .map(e -> new PortalItemEntity(e.getKey(), e.getValue()))
            .collect(Collectors.toList()));

        // load and refresh
        getItems().forEach(item -> {
            PortalItem portalItem = item.getPortalItem();
            portalItem.loadAsync();
            portalItem.addDoneLoadingListener(() -> getItems().replaceAll(i -> i));
        });

        portalItems.addListener((MapChangeListener<UUID, PortalItem>) change -> {
            if (change.wasAdded()) {
                getItems().add(new PortalItemEntity(change.getKey(), change.getValueAdded()));
            } else if (change.wasRemoved()) {
                getItems().removeIf(i -> i.getId().equals(change.getKey()));
            }

            // refresh
            getItems().forEach(item -> {
                PortalItem portalItem = item.getPortalItem();
                portalItem.loadAsync();
                portalItem.addDoneLoadingListener(() -> getItems().replaceAll(i -> i));
            });
        });

        getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                eventService.emit(new SelectEditorEvent(newValue.getId(), workspace));
            }
        }));
    }
}
