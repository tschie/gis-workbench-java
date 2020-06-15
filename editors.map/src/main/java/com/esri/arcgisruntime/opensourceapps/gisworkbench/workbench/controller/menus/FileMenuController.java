package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.controller.menus;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.Activator;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.model.Context;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspaces.service.WorkspacesService;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.stage.DirectoryChooser;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import java.io.File;

public class FileMenuController {

    @Inject
    private Context context;

    @Inject
    private WorkspacesService workspacesService;

    @FXML
    private void handleExit() {
        Alert closeConfirmationDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you" +
                " want to exit?");
        closeConfirmationDialog.setTitle("Exit");
        closeConfirmationDialog.setHeaderText("");
        closeConfirmationDialog.showAndWait().ifPresent(buttonType -> {
            if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                try {
                    Bundle thisBundle = FrameworkUtil.getBundle(Activator.class);
                    if (thisBundle != null) {
                        thisBundle.getBundleContext().getBundle(0).stop();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void handleNewWorkspace() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("New workspace");
        File file = directoryChooser.showDialog(context.getWindow());
        if (file != null) {
            workspacesService.open(file);
        }
    }

    @FXML
    private void handleOpen() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open workspace");
        File file = directoryChooser.showDialog(context.getWindow());
        if (file != null) {
            workspacesService.open(file);
        }
    }

    @FXML
    private void handleSettings() {

    }
}
