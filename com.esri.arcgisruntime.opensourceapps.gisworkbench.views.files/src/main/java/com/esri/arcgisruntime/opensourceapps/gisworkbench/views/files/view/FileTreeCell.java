/*
 * Copyright 2020 Esri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.files.view;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.files.Activator;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.files.model.OpenEvent;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import org.osgi.framework.FrameworkUtil;

import java.io.File;
import java.io.IOException;

public class FileTreeCell extends TreeCell<File> {

    private final Workspace workspace;
    private final EventService eventService;

    public FileTreeCell(Workspace workspace, EventService eventService) {
        this.workspace = workspace;
        this.eventService = eventService;
    }

    @Override
    protected void updateItem(File file, boolean b) {
        super.updateItem(file, b);
        if (!b && file != null) {
            setText(file.getName());
            ImageView imageView = new ImageView();
            setGraphic(imageView);
            try {
                if (file.isDirectory()) {
                    imageView.setImage(new Image(FrameworkUtil.getBundle(Activator.class).getResource(
                            "/images/folder-outline.png").openStream(), 14, 14, true, true));
                } else {
                    imageView.setImage(new Image(FrameworkUtil.getBundle(Activator.class).getResource(
                            "/images/file-outline.png").openStream(), 14, 14, true, true));
                }
            } catch (IOException ex) {
                // do nothing
            }
            setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                    eventService.emit(new OpenEvent(file, workspace));
                }
            });
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}
