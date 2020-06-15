package com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.project.modules;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.project.view.ProjectPerspective;
import com.google.inject.AbstractModule;

import java.util.Objects;

public class ProjectPerspectiveModule extends AbstractModule {

    private final ProjectPerspective projectPerspective;

    public ProjectPerspectiveModule(ProjectPerspective projectPerspective) {
        this.projectPerspective = Objects.requireNonNull(projectPerspective);
    }

    @Override
    protected void configure() {
        bind(ProjectPerspective.class).toInstance(projectPerspective);
    }
}