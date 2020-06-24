package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.controller;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.Perspective;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.view.Workbench;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.Objects;
import java.util.Optional;

public class ViewPaneController {

    @FXML
    private VBox vBox;

    @Inject
    private Workbench workbench;

    @Inject
    private ViewService viewService;

    @FXML
    private void initialize() {
        if (workbench.getWorkspace() != null) {
            changePerspective(workbench.getPerspective());
            workbench.perspectiveProperty().addListener((observable, oldValue, newValue) -> changePerspective(newValue));
        }
    }

    private void changePerspective(Perspective perspective) {
        vBox.getChildren().clear();
        if (perspective != null) {
            perspective.getLeftPanels().stream()
                    .map(this::createTitledPane)
                    .filter(Objects::nonNull)
                    .forEach(titledPane -> vBox.getChildren().add(titledPane));
        }
    }

    private TitledPane createTitledPane(String panelName) {
        // TODO: watch for new views
        Optional<ViewProviderService> viewProviderOptional = viewService.getAll().stream()
                .filter(viewProviderService -> viewProviderService.getName().equals(panelName))
                .findFirst();
        if (viewProviderOptional.isPresent()) {
            ViewProviderService viewProviderService = viewProviderOptional.get();
            TitledPane titledPane = new TitledPane();
            VBox.setVgrow(titledPane, Priority.ALWAYS);
            titledPane.setText(viewProviderService.getName());
            titledPane.setContent(viewProviderService.createNodeForWorkspace(workbench.getWorkspace()));
            return titledPane;
        } else {
            return null;
        }
    }
}
