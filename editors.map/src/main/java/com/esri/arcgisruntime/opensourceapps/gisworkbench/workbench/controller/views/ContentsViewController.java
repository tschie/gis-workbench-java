package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.controller.views;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.model.Context;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.service.PortalItemComponentService;
import com.esri.arcgisruntime.portal.PortalItem;
import com.google.inject.Inject;
import javafx.collections.MapChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ContentsViewController {

    @Inject
    private PortalItemComponentService portalItemComponentService;

    @Inject
    private Context context;

    @FXML
    private TreeView<UUID> treeView;

    @FXML
    private void initialize() {
        TreeItem<UUID> root = new TreeItem<>();
        root.setExpanded(true);
        treeView.setRoot(root);
        treeView.setShowRoot(false);

        treeView.setCellFactory(c -> new PortalItemTreeCell(portalItemComponentService));

        root.getChildren().addAll(portalItemComponentService.getAllEntries().stream()
                .map(Map.Entry::getKey)
                .map(TreeItem::new)
                .collect(Collectors.toList())
        );

        portalItemComponentService.observableMapProperty().addListener((MapChangeListener<UUID, PortalItem>) change -> {
            root.getChildren().clear();
            if (change.wasAdded()) {
                root.getChildren().add(new TreeItem<>(change.getKey()));
            } else if (change.wasRemoved()) {
                root.getChildren().removeIf(i -> i.getValue().equals(change.getKey()));
            }
        });

        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                context.setSelection(newValue.getValue());
            }
        });
    }
}
