package com.esri.arcgisruntime.opensourceapps.gisworkbench.editors.map.impl;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service.Editor;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.scene.Node;
import javafx.scene.control.Alert;

public class MapEditor extends Editor {

    private final Node node;

    public MapEditor(Workspace workspace, String id, Object data) {
        super(workspace, id, data);
        MapView mapView = new MapView();
        if (data instanceof ArcGISMap) {
            ArcGISMap map = (ArcGISMap) data;
            map.loadAsync();
            map.addDoneLoadingListener(() -> {
                if (map.getLoadStatus() == LoadStatus.LOADED && map.getItem() != null) {
                    setDisplayName(map.getItem().getTitle());
                }
            });
            mapView.setMap(map);
        } else if (data instanceof PortalItem && ((PortalItem) data).getType() == PortalItem.Type.WEBMAP) {
            PortalItem portalItem = (PortalItem) data;
            portalItem.loadAsync();
            portalItem.addDoneLoadingListener(() -> {
                if (portalItem.getLoadStatus() == LoadStatus.LOADED) {
                    setDisplayName(portalItem.getTitle());
                }
            });
            mapView.setMap(new ArcGISMap(portalItem));
        }
        this.node = mapView;
    }

    @Override
    public String getName() {
        return "Map";
    }

    @Override
    public Node getNode() {
        return node;
    }
}
