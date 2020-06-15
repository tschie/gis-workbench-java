package com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.project.modules;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewService;
import com.google.inject.AbstractModule;

import java.util.Objects;

public class ViewServiceModule extends AbstractModule {

    private final ViewService viewService;

    public ViewServiceModule(ViewService viewService) {
        this.viewService = Objects.requireNonNull(viewService);
    }

    @Override
    protected void configure() {
        bind(ViewService.class).toInstance(viewService);
    }
}