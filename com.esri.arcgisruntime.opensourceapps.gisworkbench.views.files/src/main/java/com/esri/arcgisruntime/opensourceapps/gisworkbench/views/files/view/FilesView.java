package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.files.view;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.files.model.SelectEvent;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;

public class FilesView extends TreeView<File> {

    public FilesView(Workspace workspace, EventService eventService) {
        setCellFactory((c) -> new FileTreeCell(workspace, eventService));

        if (workspace != null) {
            TreeItem<File> root = new FileTreeItem(workspace.getRootDirectory());
            root.setExpanded(true);
            setRoot(root);
        } else {
            setRoot(null);
        }

        getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                eventService.emit(new SelectEvent(newValue.getValue(), workspace));
            }
        }));
    }
}
