package com.esri.arcgisruntime.opensourceapps.gisworkbench.editors.map.impl;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service.Editor;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;

import java.util.UUID;

public class MapEditor extends Editor {

    private final ArcGISMap map;

    public MapEditor(Workspace workspace, ArcGISMap map, UUID id) {
        // TODO: serialize editor state such as last viewpoint
        super(workspace, id, "Map", new MapView());
        this.map = map;
        MapView mapView = (MapView) getNode();
        map.loadAsync();
        map.addDoneLoadingListener(() -> {
            if (map.getLoadStatus() == LoadStatus.LOADED && map.getItem() != null) {
                setDisplayText(map.getItem().getTitle());
            }
        });
        mapView.setMap(map);
    }

    public ArcGISMap getMap() {
        return map;
    }
}
