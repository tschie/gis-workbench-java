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

package com.esri.arcgisruntime.opensourceapps.workbench.filemenu.impl;

import com.esri.arcgisruntime.opensourceapps.workbench.core.repository.ObservableRepository;
import com.esri.arcgisruntime.opensourceapps.workbench.filemenu.service.FileMenuItemProvider;
import com.esri.arcgisruntime.opensourceapps.workbench.filemenu.service.FileMenuService;
import com.esri.arcgisruntime.opensourceapps.workbench.menu.service.MenuProvider;
import com.esri.arcgisruntime.opensourceapps.workbench.stage.service.StageService;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collection;
import java.util.stream.Collectors;

@Component(immediate = true)
public class FileMenuProviderImpl implements MenuProvider {

    private final ReadOnlyObjectWrapper<Menu> fileMenu = new ReadOnlyObjectWrapper<>(new Menu("File"));
    @Reference
    private StageService stageService;
    @Reference
    private FileMenuService fileMenuService;

    @Activate
    private void activate() {
        Menu menu = fileMenu.get();
        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(e -> {
            Stage stage = stageService.getStage();
            if (stage != null) {
                stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
            }
        });
        menu.getItems().add(exitMenuItem);

        menu.getItems().addAll(0, fileMenuService.getAll().stream()
                .map(ObservableRepository::getAll)
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        fileMenuService.observableListProperty().addListener((ListChangeListener<FileMenuItemProvider>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    menu.getItems().addAll(0, change.getAddedSubList().stream()
                            .map(ObservableRepository::getAll)
                            .flatMap(Collection::stream)
                            .collect(Collectors.toList())
                    );
                } else if (change.wasRemoved()) {
                    menu.getItems().remove(change.getFrom(), change.getTo());
                }
            }
        });
    }

    @Override
    public ReadOnlyObjectProperty<Menu> objectProperty() {
        return fileMenu;
    }
}
