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

package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.service.FileComponentProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.service.FileComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.osgi.service.component.annotations.*;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Component(immediate = true)
public class FileComponentServiceImpl implements FileComponentService {
    
    private final ReadOnlyMapWrapper<Workspace, ObservableMap<UUID, File>> fileComponentsByWorkspace =
        new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());

    private final ReadOnlyListWrapper<FileComponentProviderService> fileComponentProviderServices = new ReadOnlyListWrapper<>(
            FXCollections.observableList(new CopyOnWriteArrayList<>())
    );

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addFileComponentProvider(FileComponentProviderService fileComponentProviderService) {
        fileComponentProviderServices.add(fileComponentProviderService);
    }

    public void removeFileComponentProvider(FileComponentProviderService fileComponentProviderService) {
        fileComponentProviderServices.remove(fileComponentProviderService);
    }
    
    @Activate
    private void activate() {
        // subscribe to and mirror all providers
        fileComponentProviderServices.forEach(this::subscribeToProvider);
        fileComponentProviderServices.addListener((ListChangeListener<FileComponentProviderService>) providersChange -> {
            while (providersChange.next()) {
                if (providersChange.wasAdded()) {
                    providersChange.getAddedSubList().forEach(this::subscribeToProvider);
                } else if (providersChange.wasRemoved()) {
                    providersChange.getRemoved().forEach(this::unsubscribeFromProvider);
                }
            }
        });
    }

    /**
     * Subscribe to a provider by forwarding all of its component changes to this provider.
     * 
     * @param fileComponentProviderService file component provider service to subscribe
     */
    private void subscribeToProvider(FileComponentProviderService fileComponentProviderService) {
        fileComponentProviderService.getAllEntries().forEach(e ->
            fileComponentsByWorkspace.get(e.getKey()).putAll(e.getValue())
        );
        fileComponentProviderService.observableMapProperty().addListener((MapChangeListener<Workspace, ObservableMap<UUID, File>>) change -> {
            if (change.wasAdded()) {
                change.getValueAdded().addListener((MapChangeListener<UUID, File>) change1 -> {
                    if (change1.wasAdded()) {
                        fileComponentsByWorkspace.get(change.getKey()).put(change1.getKey(), change1.getValueAdded());
                    } else if (change1.wasRemoved()) {
                        fileComponentsByWorkspace.get(change.getKey()).remove(change1.getKey());
                    }
                });
            } else if (change.wasRemoved()) {
                // remove components from removed workspace
                change.getValueRemoved().forEach((key, value) -> fileComponentsByWorkspace.get(change.getKey()).remove(key));
            }
        });
    }

    /**
     * Unsubscribe from a provider by removing all references to the provider's components.
     * 
     * @param fileComponentProviderService file component provider service to unsubscribe
     */
    private void unsubscribeFromProvider(FileComponentProviderService fileComponentProviderService) {
        fileComponentProviderService.getAllEntries().forEach(entry ->
            entry.getValue().forEach((key, value) -> fileComponentsByWorkspace.get(entry.getKey()).remove(key))
        );
    }
    
    @Override
    public ObservableMap<UUID, File> get(Workspace workspace) {
        // create a map for this workspace to track components if it does not already exist
        fileComponentsByWorkspace.computeIfAbsent(workspace, k -> {
            ObservableMap<UUID, File> components = FXCollections.observableHashMap();
            fileComponentProviderServices.stream()
                .map(fileComponentProviderService -> fileComponentProviderService.get(workspace))
                .forEach(components::putAll);
            return components;
        });
        // get the component map for the workspace
        return fileComponentsByWorkspace.get(workspace);
    }

    @Override
    public ReadOnlyMapProperty<Workspace, ObservableMap<UUID, File>> observableMapProperty() {
        return fileComponentsByWorkspace.getReadOnlyProperty();
    }

    @Override
    public Set<Map.Entry<Workspace, ObservableMap<UUID, File>>> getAllEntries() {
        return fileComponentsByWorkspace.entrySet();
    }
}
