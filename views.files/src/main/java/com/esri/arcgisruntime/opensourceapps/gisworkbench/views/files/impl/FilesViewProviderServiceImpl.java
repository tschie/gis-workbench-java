package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.files.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.service.FileComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.files.view.FilesTreeView;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.scene.Node;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class FilesViewProviderServiceImpl implements ViewProviderService {

    @Reference
    private EventService eventService;

    @Reference
    private FileComponentService fileComponentService;

    @Override
    public String getName() {
        return "Files";
    }

    @Override
    public Node createNodeForWorkspace(Workspace workspace) {
        return new FilesTreeView(workspace, eventService, fileComponentService);
    }

    @Override
    public String toString() {
        return getName();
    }
}
