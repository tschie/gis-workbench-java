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

import com.esri.arcgisruntime.opensourceapps.workbench.filemenu.service.FileMenuItemProvider;
import com.esri.arcgisruntime.opensourceapps.workbench.filemenu.service.FileMenuService;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.concurrent.CopyOnWriteArrayList;

@Component(immediate = true)
public class FileMenuServiceImpl implements FileMenuService {

    private final ReadOnlyListWrapper<FileMenuItemProvider> fileMenuItemProviders = new ReadOnlyListWrapper<>(
            FXCollections.observableList(new CopyOnWriteArrayList<>())
    );

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addFileMenuItemProvider(FileMenuItemProvider fileMenuItemProvider) {
        fileMenuItemProviders.add(fileMenuItemProvider);
    }

    public void removeFileMenuItemProvider(FileMenuItemProvider fileMenuItemProvider) {
        fileMenuItemProviders.remove(fileMenuItemProvider);
    }

    @Override
    public ReadOnlyListProperty<FileMenuItemProvider> observableListProperty() {
        return fileMenuItemProviders.getReadOnlyProperty();
    }
}
