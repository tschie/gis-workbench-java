package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.Activator;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.modules.*;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.PerspectiveService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.WorkspaceService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.view.Workbench;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service.EditorService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.google.inject.Inject;
import com.google.inject.Module;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component(immediate = true, service = WorkbenchService.class)
public class WorkbenchService {

    private final ReadOnlyListWrapper<Workbench> workbenches =
            new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

    @Reference
    private WorkspaceService workspaceService;

    @Reference
    private PerspectiveService perspectiveService;

    @Reference
    private ViewService viewService;

    @Reference
    private EditorService editorService;

    @Reference
    private EventService eventService;

    @Inject
    private FXMLLoader fxmlLoader;

    private final List<Module> serviceModules = new ArrayList<>();

    @Activate
    private void activate() {
        serviceModules.addAll(Arrays.asList(
            new EditorServiceModule(editorService),
            new EventServiceModule(eventService),
            new PerspectiveServiceModule(perspectiveService),
            new ViewServiceModule(viewService),
            new WorkspaceServiceModule(workspaceService)
        ));

        workspaceService.getActiveWorkspaces().forEach(workspace ->
            Platform.runLater(() -> {
                Workbench workbench = createWorkbench(workspace);
                workbench.show();
                workbenches.add(workbench);
            })
        );

        if (workspaceService.getActiveWorkspaces().isEmpty()) {
            Platform.runLater(() -> {
                Workbench workbench = createEmptyWorkbench();
                workbench.show();
                workspaceService.activeWorkspacesProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null && !newValue.isEmpty()) {
                        workbench.close();
                    }
                });
            });
        }

        workspaceService.activeWorkspacesProperty().addListener((ListChangeListener<Workspace>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(workspace -> {
                        Workbench workbench = createWorkbench(workspace);
                        workbench.show();
                        workbenches.add(workbench);
                    });
                }
            }
        });
    }

    private Workbench createWorkbench(Workspace workspace) {
        Workbench workbench = new Workbench(Objects.requireNonNull(workspace), perspectiveService, serviceModules);
        workbench.setTitle(workspace.getRootDirectory().getName() + " - " + workspace.getRootDirectory().getAbsolutePath());
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
                workspaceService.close(workspace);
            }
        });
        return workbench;
    }

    private Workbench createEmptyWorkbench() {
        Workbench workbench = new Workbench(null, perspectiveService, serviceModules);
        workbench.setTitle("GIS Workbench");
        workbench.setOnCloseRequest(windowEvent -> {
            Alert closeConfirmationDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
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
