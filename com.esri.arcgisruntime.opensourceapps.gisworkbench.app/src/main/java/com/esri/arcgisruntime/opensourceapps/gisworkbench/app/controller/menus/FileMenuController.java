package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.controller.menus;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.Activator;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.WorkspaceService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.view.Workbench;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
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
    private Workbench workbench;

    @Inject
    private WorkspaceService workspacesService;

    @FXML
    private void handleExit() {
        Alert closeConfirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        closeConfirmationDialog.initOwner(workbench.getOwner());
        closeConfirmationDialog.setTitle("Exit");
        closeConfirmationDialog.setHeaderText("");
        closeConfirmationDialog.setContentText("Are you sure you want to exit?");
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
        File file = directoryChooser.showDialog(workbench.getOwner());
        if (file != null) {
            workspacesService.open(new Workspace(file, file.toPath().resolve(".gisworkbench").toFile()));
        }
    }

    @FXML
    private void handleOpen() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open workspace");
        File file = directoryChooser.showDialog(workbench.getOwner());
        if (file != null) {
            workspacesService.open(new Workspace(file, file.toPath().resolve(".gisworkbench").toFile()));
        }
    }

    @FXML
    private void handleSettings() {

    }
}
