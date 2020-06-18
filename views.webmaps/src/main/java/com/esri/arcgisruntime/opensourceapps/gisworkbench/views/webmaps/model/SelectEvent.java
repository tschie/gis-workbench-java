package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.model;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.Event;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;

import java.util.List;

public class SelectEvent implements Event {

    private final ArcGISMap map;
    private final Workspace workspace;

    public SelectEvent(ArcGISMap map, Workspace workspace) {
        this.map = map;
        this.workspace = workspace;
    }

    public ArcGISMap getArcGISMap() {
        return map;
    }

    @Override
    public Workspace getWorkspace() {
        return workspace;
    }

    @Override
    public List<String> getEventTypes() {
        return List.of("select", "click", getClass().getName());
    }

    @Override
    public Object getData() {
        return getArcGISMap();
    }
}
