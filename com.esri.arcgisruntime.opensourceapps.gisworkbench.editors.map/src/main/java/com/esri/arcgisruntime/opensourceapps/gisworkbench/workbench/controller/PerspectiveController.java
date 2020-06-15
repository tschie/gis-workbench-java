package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.controller;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class PerspectiveController {

    @FXML
    private StackPane stackPane;

    /*@FXML
    void initialize() {

        if (projectService.getProject() != null) {
            stackPane.getChildren().clear();
            // TODO: check metadata for last perspective
            promptPerspectiveChange();
        }

        projectService.projectProperty().addListener((observable, oldValue, newValue) -> {
            stackPane.getChildren().clear();
            if (newValue != null) {
                promptPerspectiveChange();
            }
        });

        if (perspectiveService.getCurrentPerspective() != null) {
            stackPane.getChildren().clear();
            stackPane.getChildren().add(perspectiveService.getCurrentPerspective().getRoot());
        }

        perspectiveService.currentPerspectiveProperty().addListener((observable, oldValue, newValue) -> {
            stackPane.getChildren().clear();
            if (newValue != null) {
                stackPane.getChildren().add(newValue.getRoot());
            }
        });
    }

    private void promptPerspectiveChange() {
        ChoiceDialog<PerspectiveProviderService> perspectiveProviderChoiceDialog = new ChoiceDialog<>();
        perspectiveProviderChoiceDialog.setTitle("Choose perspective");
        perspectiveProviderChoiceDialog.setContentText("Choose a perspective");
        perspectiveProviderChoiceDialog.getItems().addAll(perspectiveService.getAll());
        Optional<PerspectiveProviderService> chosenPerspectiveProvider = perspectiveProviderChoiceDialog.showAndWait();
        chosenPerspectiveProvider.ifPresent(provider -> perspectiveService.setCurrentPerspective(provider.create()));
    }*/

}
