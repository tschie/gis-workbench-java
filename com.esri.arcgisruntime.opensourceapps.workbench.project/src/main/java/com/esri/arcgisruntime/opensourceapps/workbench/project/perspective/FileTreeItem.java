package com.esri.arcgisruntime.opensourceapps.workbench.project.perspective;

import javafx.scene.control.TreeItem;

import java.io.File;

public class FileTreeItem extends TreeItem<File> {

    public FileTreeItem(File file) {
        super(file);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    getChildren().add(new FileTreeItem(f));
                }
            }
        }
    }
}
