package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.controller.menus;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.model.Context;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.service.PortalItemComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.view.PortalItemImportDialog;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Menu;

import java.util.UUID;

public class ImportMenuController {

    @FXML
    private Menu menu;

    @Inject
    private Context context;

    @Inject
    private PortalItemComponentService portalItemComponentService;

    @FXML
    private void initialize() {
        menu.setVisible(context.getWorkspace() != null);
    }

    @FXML
    private void handleImportPortalItem() {
        Platform.runLater(() -> {
            PortalItemImportDialog portalItemImportDialog = new PortalItemImportDialog();
            portalItemImportDialog.showAndWait().ifPresent(portalItem -> {
                UUID id = UUID.randomUUID();
                portalItemComponentService.put(id, portalItem);
            });
        });
    }
}
