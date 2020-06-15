package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.controller.menus;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.view.Workbench;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;

public class ImportMenuController {

    @FXML
    private Menu menu;

    @Inject
    private Workbench workbench;

    @FXML
    private void initialize() {
        menu.setVisible(workbench.getWorkspace() != null);
    }
}
