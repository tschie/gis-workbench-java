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

package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.impl;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.service.MapComponentProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.service.MapComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.osgi.service.component.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Component(immediate = true)
public class MapComponentServiceImpl implements MapComponentService {
    
    private final ReadOnlyMapWrapper<Workspace, ObservableMap<UUID, ArcGISMap>> mapComponentsByWorkspace =
        new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());

    private final ReadOnlyListWrapper<MapComponentProviderService> mapComponentProviderServices = new ReadOnlyListWrapper<>(
            FXCollections.observableList(new CopyOnWriteArrayList<>())
    );

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addMapComponentProvider(MapComponentProviderService mapComponentProviderService) {
        mapComponentProviderServices.add(mapComponentProviderService);
    }

    public void removeMapComponentProvider(MapComponentProviderService mapComponentProviderService) {
        mapComponentProviderServices.remove(mapComponentProviderService);
    }
    
    @Activate
    private void activate() {
        // subscribe to and mirror all providers
        mapComponentProviderServices.forEach(this::subscribeToProvider);
        mapComponentProviderServices.addListener((ListChangeListener<MapComponentProviderService>) providersChange -> {
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
     * @param mapComponentProviderService map component provider service to subscribe
     */
    private void subscribeToProvider(MapComponentProviderService mapComponentProviderService) {
        mapComponentProviderService.getAllEntries().forEach(e -> {
            mapComponentsByWorkspace.computeIfAbsent(e.getKey(), k -> FXCollections.observableHashMap());
            mapComponentsByWorkspace.get(e.getKey()).putAll(e.getValue());
        });
        mapComponentProviderService.observableMapProperty().addListener((MapChangeListener<Workspace, ObservableMap<UUID, ArcGISMap>>) change -> {
            mapComponentsByWorkspace.computeIfAbsent(change.getKey(), k -> FXCollections.observableHashMap());
            if (change.wasAdded()) {
                change.getValueAdded().addListener((MapChangeListener<UUID, ArcGISMap>) change1 -> {
                    if (change1.wasAdded()) {
                        mapComponentsByWorkspace.get(change.getKey()).put(change1.getKey(), change1.getValueAdded());
                    } else if (change1.wasRemoved()) {
                        mapComponentsByWorkspace.get(change.getKey()).remove(change1.getKey());
                    }
                });
            } else if (change.wasRemoved()) {
                // remove components from removed workspace
                change.getValueRemoved().forEach((key, value) -> mapComponentsByWorkspace.get(change.getKey()).remove(key));
            }
        });
    }

    /**
     * Unsubscribe from a provider by removing all references to the provider's components.
     * 
     * @param mapComponentProviderService map component provider service to unsubscribe
     */
    private void unsubscribeFromProvider(MapComponentProviderService mapComponentProviderService) {
        mapComponentProviderService.getAllEntries().forEach(entry -> {
            mapComponentsByWorkspace.computeIfAbsent(entry.getKey(), k -> FXCollections.observableHashMap());
            entry.getValue().forEach((key, value) -> mapComponentsByWorkspace.get(entry.getKey()).remove(key));
        });
    }
    
    @Override
    public ObservableMap<UUID, ArcGISMap> get(Workspace workspace) {
        // create a map for this workspace to track components if it does not already exist
        mapComponentsByWorkspace.computeIfAbsent(workspace, k -> FXCollections.observableHashMap());
        return mapComponentsByWorkspace.get(workspace);
    }

    @Override
    public ReadOnlyMapProperty<Workspace, ObservableMap<UUID, ArcGISMap>> observableMapProperty() {
        return mapComponentsByWorkspace.getReadOnlyProperty();
    }

    @Override
    public Set<Map.Entry<Workspace, ObservableMap<UUID, ArcGISMap>>> getAllEntries() {
        return mapComponentsByWorkspace.entrySet();
    }
}
