package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.controller.panes;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class RightPaneController {

    @FXML
    private VBox vBox;

    @FXML
    private void initialize() {
        vBox.managedProperty().bind(vBox.visibleProperty());
        vBox.visibleProperty().bind(Bindings.createBooleanBinding(() -> !vBox.getChildren().isEmpty(),
                vBox.getChildren()));
    }
}
