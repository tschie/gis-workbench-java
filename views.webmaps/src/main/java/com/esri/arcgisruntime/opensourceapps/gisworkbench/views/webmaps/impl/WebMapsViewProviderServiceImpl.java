package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.view.WebMapsView;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.scene.Node;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class WebMapsViewProviderServiceImpl implements ViewProviderService {

    @Reference
    private EventService eventService;

    @Override
    public String getName() {
        return "Web Maps";
    }

    @Override
    public Node createNodeForWorkspace(Workspace workspace) {
        return new WebMapsView(workspace, eventService);
    }

    @Override
    public String toString() {
        return getName();
    }
}
