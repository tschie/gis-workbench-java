package com.esri.arcgisruntime.opensourceapps.workbench.project.perspective;

import com.esri.arcgisruntime.opensourceapps.workbench.project.service.ProjectService;
import com.esri.arcgisruntime.opensourceapps.workbench.project.service.TabService;
import com.esri.arcgisruntime.opensourceapps.workbench.selection.service.SelectionService;
import com.google.inject.Inject;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;

public class PerspectiveController {

    @Inject
    private ProjectService projectService;

    @Inject
    private SelectionService selectionService;

    @Inject
    private TabService tabService;

    @FXML
    private TreeView<File> treeView;

    @FXML
    private TabPane tabPane;

    @FXML
    private void initialize() {
        treeView.setCellFactory((c) -> new FileTreeCell());

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                selectionService.setSelection(newValue.getValue());
            } else {
                selectionService.setSelection(null);
            }
        });

        updateProjectTree(projectService.getProject());

        projectService.projectProperty().addListener((observable, oldValue, newValue) -> {
            updateProjectTree(newValue);
        });

        tabPane.getTabs().addAll(tabService.getTabs());
        tabService.tabsProperty().addListener((ListChangeListener<Tab>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(tab -> tabPane.getTabs().add(tab));
                }
            }
        });
    }

    private void updateProjectTree(File projectDirectory) {
        if (projectDirectory != null) {
            TreeItem<File> root = new FileTreeItem(projectDirectory);
            root.setExpanded(true);
            treeView.setRoot(root);
        } else {
            treeView.setRoot(null);
        }
    }
}
