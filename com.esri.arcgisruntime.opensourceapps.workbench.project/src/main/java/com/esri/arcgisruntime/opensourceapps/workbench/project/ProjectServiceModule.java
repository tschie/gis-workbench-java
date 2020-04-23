package com.esri.arcgisruntime.opensourceapps.workbench.project;

import com.esri.arcgisruntime.opensourceapps.workbench.project.service.ProjectService;
import com.google.inject.AbstractModule;

public class ProjectServiceModule extends AbstractModule {

    private final ProjectService projectService;

    public ProjectServiceModule(ProjectService projectService) {
        this.projectService = projectService;
    }

    @Override protected void configure() {
        bind(ProjectService.class).toInstance(projectService);
    }
}