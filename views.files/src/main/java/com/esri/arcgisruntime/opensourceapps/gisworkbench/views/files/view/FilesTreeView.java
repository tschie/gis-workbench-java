package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.files.view;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.service.FileComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.events.editor.SelectEditorEvent;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.collections.MapChangeListener;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.io.File;
import java.util.UUID;
import java.util.stream.Collectors;

public class FilesTreeView extends TreeView<FileEntity> {

    public FilesTreeView(
        Workspace workspace,
        EventService eventService,
        FileComponentService fileComponentService
    ) {

        setCellFactory((c) -> new FileTreeCell(workspace, eventService));

        TreeItem<FileEntity> root = new TreeItem<>();
        root.setExpanded(true);
        setRoot(root);
        setShowRoot(false);

        // TODO: handle nesting
        // ignore directories, flatten
        root.getChildren().addAll(fileComponentService.get(workspace).entrySet().stream()
            .filter(e -> e.getValue().isFile())
            .map(e -> new FileEntity(e.getKey(), e.getValue()))
            .map(TreeItem::new)
            .collect(Collectors.toList()));

        fileComponentService.get(workspace).addListener((MapChangeListener<UUID, File>) change -> {
            if (change.wasAdded()) {
                if (change.getValueAdded().isFile()) {
                    root.getChildren().add(new TreeItem<>(new FileEntity(change.getKey(), change.getValueAdded())));
                }
            } else if (change.wasRemoved()) {
                root.getChildren().removeIf(item -> item.getValue().getId() == change.getKey());
            }
        });

        // TODO: create service to allow multiple providers to specify a selection action
        getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                eventService.emit(new SelectEditorEvent(newValue.getValue().getId(), workspace));
            }
        }));
    }
}
