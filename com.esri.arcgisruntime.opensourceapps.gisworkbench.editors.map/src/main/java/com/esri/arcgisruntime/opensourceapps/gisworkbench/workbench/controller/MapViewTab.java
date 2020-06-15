package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.controller;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.MapView;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;

public class MapViewTab extends Tab {

    public MapViewTab(ArcGISMap map) {
        map.addDoneLoadingListener(() -> {
            if (map.getLoadStatus() == LoadStatus.LOADED) {
                setText(map.getItem().getTitle());
            } else {
                new Alert(Alert.AlertType.ERROR, map.getLoadError().getCause().getMessage()).show();
            }
        });

        MapView mapView = new MapView();
        mapView.setMap(map);

        setContent(mapView);
    }
}
