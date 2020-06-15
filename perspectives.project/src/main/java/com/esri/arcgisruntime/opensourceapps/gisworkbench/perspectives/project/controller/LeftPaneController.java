package com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.project.controller;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.project.view.ProjectPerspective;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewService;
import com.google.inject.Inject;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class LeftPaneController {

    @FXML
    private TabPane tabPane;

    @Inject
    private ProjectPerspective projectPerspective;

    @Inject
    private ViewService viewService;

    @FXML
    private void initialize() {
        viewService.getAll().stream()
                .filter(viewProviderService -> !"Editor".equals(viewProviderService.getName()))
                .map(this::createViewTab)
                .forEach(tab -> tabPane.getTabs().add(tab));

        viewService.observableListProperty().addListener((ListChangeListener<ViewProviderService>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream()
                            .filter(viewProviderService -> !"Editor".equals(viewProviderService.getName()))
                            .map(this::createViewTab)
                            .forEach(tab -> tabPane.getTabs().add(tab));
                } else if (change.wasRemoved()) {
                    tabPane.getTabs().removeIf(tab -> change.getRemoved().contains(tab.getUserData()));
                }
            }
        });
    }

    private Tab createViewTab(ViewProviderService viewProviderService) {
        Tab tab = new Tab(viewProviderService.getName());
        tab.setUserData(viewProviderService);
        tab.setContent(viewProviderService.createNodeForWorkspace(projectPerspective.getWorkspace()));
        return tab;
    }
}
