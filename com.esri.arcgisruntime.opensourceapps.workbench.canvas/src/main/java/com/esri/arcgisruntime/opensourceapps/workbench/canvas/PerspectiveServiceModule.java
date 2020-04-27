package com.esri.arcgisruntime.opensourceapps.workbench.canvas;

import com.esri.arcgisruntime.opensourceapps.workbench.perspective.service.PerspectiveService;
import com.google.inject.AbstractModule;

public class PerspectiveServiceModule extends AbstractModule {

    private final PerspectiveService perspectiveService;

    public PerspectiveServiceModule(PerspectiveService perspectiveService) {
        this.perspectiveService = perspectiveService;
    }

    @Override
    protected void configure() {
        bind(PerspectiveService.class).toInstance(perspectiveService);
    }
}