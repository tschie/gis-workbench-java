package com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.project.controller;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.project.view.ProjectPerspective;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewService;
import com.google.inject.Inject;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class EditorPaneController {

    @FXML
    private StackPane stackPane;

    @Inject
    private ProjectPerspective projectPerspective;

    @Inject
    private ViewService viewService;

    @FXML
    private void initialize() {
        viewService.getAll().stream()
                .filter(viewProviderService -> "Editor".equals(viewProviderService.getName()))
                .findFirst()
                .ifPresent(viewProviderService -> stackPane.getChildren().add(viewProviderService.createNodeForWorkspace(projectPerspective.getWorkspace())));

        viewService.observableListProperty().addListener((ListChangeListener<ViewProviderService>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().stream()
                            .filter(viewProviderService -> "Editor".equals(viewProviderService.getName()))
                            .findFirst()
                            .ifPresent(viewProviderService -> stackPane.getChildren().add(viewProviderService.createNodeForWorkspace(projectPerspective.getWorkspace())));
                } else if (change.wasRemoved()) {
                    change.getRemoved().stream()
                            .filter(viewProviderService -> "Editor".equals(viewProviderService.getName()))
                            .findAny()
                            .ifPresent(viewProviderService -> stackPane.getChildren().clear());
                }
            }
        });
    }
}
