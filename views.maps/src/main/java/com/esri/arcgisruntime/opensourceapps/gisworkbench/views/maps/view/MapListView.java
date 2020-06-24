package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.maps.view;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.service.MapComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.events.editor.SelectEditorEvent;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.maps.model.MapEntity;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.scene.control.ListView;

import java.util.UUID;
import java.util.stream.Collectors;

public class MapListView extends ListView<MapEntity> {

    private final ObservableMap<UUID, ArcGISMap> maps;
    private final EventService eventService;
    private final Workspace workspace;

    public MapListView(Workspace workspace, EventService eventService, MapComponentService mapComponentService) {

        this.workspace = workspace;
        this.eventService = eventService;
        setCellFactory((c) -> new MapListCell(workspace, eventService));

        maps = mapComponentService.get(workspace);
        getItems().addAll(maps.entrySet().stream()
            .map(e -> new MapEntity(e.getKey(), e.getValue()))
            .collect(Collectors.toList()));

        // load and refresh
        getItems().forEach(item -> {
            ArcGISMap arcgisMap = item.getMap();
            arcgisMap.loadAsync();
            arcgisMap.addDoneLoadingListener(() -> getItems().replaceAll(i -> i));
        });

        maps.addListener((MapChangeListener<UUID, ArcGISMap>) change -> {
            if (change.wasAdded()) {
                getItems().add(new MapEntity(change.getKey(), change.getValueAdded()));
            } else if (change.wasRemoved()) {
                getItems().removeIf(i -> i.getId().equals(change.getKey()));
            }

            // refresh
            getItems().forEach(item -> {
                ArcGISMap map = item.getMap();
                map.loadAsync();
                map.addDoneLoadingListener(() -> getItems().replaceAll(i -> i));
            });
        });

        getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                eventService.emit(new SelectEditorEvent(newValue.getId(), workspace));
            }
        }));
    }
}
