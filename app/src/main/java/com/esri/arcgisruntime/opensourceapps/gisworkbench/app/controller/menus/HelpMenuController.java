package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.controller.menus;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.awt.*;
import java.net.URL;

public class HelpMenuController {

    @FXML
    private void handleHelp() {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URL("https://github.com/Esri/gis-workbench-java").toURI());
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Error opening URL.").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Opening URL not supported by this device.").show();
        }
    }
}
