package com.esri.arcgisruntime.opensourceapps.workbench.project;

import com.esri.arcgisruntime.opensourceapps.workbench.selection.service.SelectionService;
import com.google.inject.AbstractModule;

public class SelectionServiceModule extends AbstractModule {

    private final SelectionService selectionService;

    public SelectionServiceModule(SelectionService selectionService) {
        this.selectionService = selectionService;
    }

    @Override protected void configure() {
        bind(SelectionService.class).toInstance(selectionService);
    }
}