package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.portalitems.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.service.PortalItemComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.workspace.service.WorkspacePortalItemComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.portalitems.view.PortalItemListView;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.scene.Node;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class PortalItemsViewProviderServiceImpl implements ViewProviderService {

    @Reference
    private EventService eventService;

    @Reference
    private PortalItemComponentService portalComponentService;

    @Reference
    private WorkspacePortalItemComponentService workspacePortalComponentService;

    @Override
    public String getName() {
        return "Portal Items";
    }

    @Override
    public Node createNodeForWorkspace(Workspace workspace) {
        return new PortalItemListView(workspace, eventService, portalComponentService, workspacePortalComponentService);
    }
}
