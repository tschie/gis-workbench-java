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

package com.esri.arcgisruntime.opensourceapps.workbench.project.impl;

import com.esri.arcgisruntime.opensourceapps.workbench.filemenu.service.FileMenuItemProvider;
import com.esri.arcgisruntime.opensourceapps.workbench.project.service.ProjectService;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.MenuItem;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class FileMenuItemProviderImpl implements FileMenuItemProvider {

    private final MenuItem openProjectMenuItem;
    private final ReadOnlyListWrapper<MenuItem> menuItems;

    @Reference
    private ProjectService projectService;

    public FileMenuItemProviderImpl() {
        openProjectMenuItem = new MenuItem("Open...");
        menuItems = new ReadOnlyListWrapper<>(FXCollections.observableArrayList(openProjectMenuItem));
    }

    @Activate
    private void activate() {
        openProjectMenuItem.setOnAction(e -> projectService.requestChangeProject());
    }

    @Override
    public ReadOnlyListProperty<MenuItem> observableListProperty() {
        return menuItems.getReadOnlyProperty();
    }
}
