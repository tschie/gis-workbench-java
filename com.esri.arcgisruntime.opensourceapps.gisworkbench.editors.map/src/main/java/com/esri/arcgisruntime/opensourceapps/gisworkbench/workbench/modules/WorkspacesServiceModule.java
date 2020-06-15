package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.modules;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspaces.service.WorkspacesService;
import com.google.inject.AbstractModule;

public class WorkspacesServiceModule extends AbstractModule {

    private final WorkspacesService workspacesService;

    public WorkspacesServiceModule(WorkspacesService workspacesService) {
        this.workspacesService = workspacesService;
    }

    @Override
    protected void configure() {
        bind(WorkspacesService.class).toInstance(workspacesService);
    }
}