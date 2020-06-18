package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.model;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.Event;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;

import java.util.List;

public class OpenEvent implements Event {

    private final ArcGISMap map;
    private final Workspace workspace;

    public OpenEvent(ArcGISMap map, Workspace workspace) {
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
        return List.of("open", "dblclick", getClass().getName());
    }

    @Override
    public Object getData() {
        return getArcGISMap();
    }
}
