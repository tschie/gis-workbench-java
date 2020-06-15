package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.modules;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.WorkspaceService;
import com.google.inject.AbstractModule;

public class WorkspaceServiceModule extends AbstractModule {

    private final WorkspaceService workspaceService;

    public WorkspaceServiceModule(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @Override
    protected void configure() {
        bind(WorkspaceService.class).toInstance(workspaceService);
    }
}