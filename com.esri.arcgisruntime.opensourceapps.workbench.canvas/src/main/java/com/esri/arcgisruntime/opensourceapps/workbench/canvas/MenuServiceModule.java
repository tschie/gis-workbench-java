package com.esri.arcgisruntime.opensourceapps.workbench.canvas;

import com.esri.arcgisruntime.opensourceapps.workbench.menu.service.MenuService;
import com.google.inject.AbstractModule;

public class MenuServiceModule extends AbstractModule {

    private final MenuService menuService;

    public MenuServiceModule(MenuService menuService) {
        this.menuService = menuService;
    }

    @Override
    protected void configure() {
        bind(MenuService.class).toInstance(menuService);
    }
}