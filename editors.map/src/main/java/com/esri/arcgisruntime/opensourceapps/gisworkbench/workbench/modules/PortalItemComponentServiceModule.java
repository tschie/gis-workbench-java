package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.modules;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.service.PortalItemComponentService;
import com.google.inject.AbstractModule;

public class PortalItemComponentServiceModule extends AbstractModule {

    private final PortalItemComponentService portalItemComponentService;

    public PortalItemComponentServiceModule(PortalItemComponentService portalItemComponentService) {
        this.portalItemComponentService = portalItemComponentService;
    }

    @Override
    protected void configure() {
        bind(PortalItemComponentService.class).toInstance(portalItemComponentService);
    }
}