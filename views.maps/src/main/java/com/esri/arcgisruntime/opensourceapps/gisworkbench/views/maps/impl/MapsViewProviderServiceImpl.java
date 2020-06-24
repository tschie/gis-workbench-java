package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.maps.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.service.MapComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.maps.view.MapListView;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.scene.Node;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class MapsViewProviderServiceImpl implements ViewProviderService {

    @Reference
    private EventService eventService;

    @Reference
    private MapComponentService mapComponentService;

    @Override
    public String getName() {
        return "Maps";
    }

    @Override
    public Node createNodeForWorkspace(Workspace workspace) {
        return new MapListView(workspace, eventService, mapComponentService);
    }
}
