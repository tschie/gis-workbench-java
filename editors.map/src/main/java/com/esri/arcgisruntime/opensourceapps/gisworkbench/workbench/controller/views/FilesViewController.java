package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.controller.views;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.model.Context;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;

public class FilesViewController {

    @FXML
    private TreeView<File> treeView;

    @Inject
    private Context context;

    @FXML
    private void initialize() {
        treeView.setCellFactory((c) -> new FileTreeCell());

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                context.setSelection(newValue.getValue());
            }
        });

        if (context.getWorkspace() != null) {
            TreeItem<File> root = new FileTreeItem(context.getWorkspace());
            root.setExpanded(true);
            treeView.setRoot(root);
        } else {
            treeView.setRoot(null);
        }
    }
}
