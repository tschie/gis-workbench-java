package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.launch.service.LaunchService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.Activator;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.service.PortalItemComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.view.Workbench;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspaces.service.WorkspacesService;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.File;
import java.util.Objects;

@Component(immediate = true, service = WorkspacesService.class)
public class WorkbenchService {

    private final ReadOnlyListWrapper<Workbench> workbenches =
            new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

    @Reference
    private LaunchService launchService;

    @Reference
    private WorkspacesService workspacesService;

    @Reference
    private PortalItemComponentService portalItemComponentService;

    @Inject
    private FXMLLoader fxmlLoader;

    @Activate
    private void activate() {
        workspacesService.getActiveWorkspaces().forEach(workspace ->
            Platform.runLater(() -> {
                Workbench workbench = createWorkbench(workspace);
                workbench.show();
                workbenches.add(workbench);
            })
        );

        if (workspacesService.getActiveWorkspaces().isEmpty()) {
            Platform.runLater(() -> {
                Workbench workbench = createEmptyWorkbench();
                workbench.show();
                workspacesService.activeWorkspacesProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null && !newValue.isEmpty()) {
                        workbench.close();
                    }
                });
            });
        }

        workspacesService.activeWorkspacesProperty().addListener((ListChangeListener<File>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(workspace -> {
                        Workbench workbench = createWorkbench(workspace);
                        workbench.show();
                    });
                }
            }
        });
    }

    private Workbench createWorkbench(File workspace) {
        Workbench workbench = new Workbench(Objects.requireNonNull(workspace), workspacesService, portalItemComponentService);
        workbench.setTitle(workspace.getName() + " - " + workspace.getAbsolutePath());
        workbench.setOnCloseRequest(windowEvent -> {
            if (workbenches.size() == 1) {
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
                    } else {
                        windowEvent.consume();
                    }
                });
            } else {
                workspacesService.close(workspace);
            }
        });
        return workbench;
    }

    private Workbench createEmptyWorkbench() {
        Workbench workbench = new Workbench(null, workspacesService, portalItemComponentService);
        workbench.setTitle("GIS Workbench");
        workbench.setOnCloseRequest(windowEvent -> {
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
                } else {
                    windowEvent.consume();
                }
            });
        });
        return workbench;
    }
}
