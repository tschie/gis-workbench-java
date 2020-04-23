package com.esri.arcgisruntime.opensourceapps.workbench.project;

import com.esri.arcgisruntime.opensourceapps.workbench.project.service.TabService;
import com.google.inject.AbstractModule;

public class TabServiceModule extends AbstractModule {

    private final TabService tabService;

    public TabServiceModule(TabService tabService) {
        this.tabService = tabService;
    }

    @Override protected void configure() {
        bind(TabService.class).toInstance(tabService);
    }
}