package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.controller;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.view.Workbench;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewService;
import com.google.inject.Inject;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class EditorController {

    @FXML
    private StackPane stackPane;

    @Inject
    private Workbench workbench;

    @Inject
    private ViewService viewService;

    @FXML
    private void initialize() {
        if (workbench.getWorkspace() != null) {
            viewService.getAll().forEach(viewProviderService -> System.out.println(viewProviderService.getName()));
            viewService.getAll().stream()
                    .filter(viewProviderService -> "Editor".equals(viewProviderService.getName()))
                    .findFirst()
                    .ifPresent(viewProviderService -> stackPane.getChildren().add(viewProviderService.createNodeForWorkspace(workbench.getWorkspace())));

            viewService.observableListProperty().addListener((ListChangeListener<ViewProviderService>) change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        change.getAddedSubList().stream()
                                .filter(viewProviderService -> "Editor".equals(viewProviderService.getName()))
                                .findFirst()
                                .ifPresent(viewProviderService -> stackPane.getChildren().add(viewProviderService.createNodeForWorkspace(workbench.getWorkspace())));
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
}
