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

package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.service.PortalItemComponentProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.service.PortalItemComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.portal.PortalItem;
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
public class PortalItemComponentServiceImpl implements PortalItemComponentService {
    
    private final ReadOnlyMapWrapper<Workspace, ObservableMap<UUID, PortalItem>> portalComponentsByWorkspace =
        new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());

    private final ReadOnlyListWrapper<PortalItemComponentProviderService> portalItemComponentProviderServices = new ReadOnlyListWrapper<>(
            FXCollections.observableList(new CopyOnWriteArrayList<>())
    );

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addPortalItemComponentProvider(PortalItemComponentProviderService portalItemComponentProviderService) {
        portalItemComponentProviderServices.add(portalItemComponentProviderService);
    }

    public void removePortalItemComponentProvider(PortalItemComponentProviderService portalItemComponentProviderService) {
        portalItemComponentProviderServices.remove(portalItemComponentProviderService);
    }
    
    @Activate
    private void activate() {
        // subscribe to and mirror all providers
        portalItemComponentProviderServices.forEach(this::subscribeToProvider);
        portalItemComponentProviderServices.addListener((ListChangeListener<PortalItemComponentProviderService>) providersChange -> {
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
     * @param portalItemComponentProviderService portal item component provider service to subscribe
     */
    private void subscribeToProvider(PortalItemComponentProviderService portalItemComponentProviderService) {
        portalItemComponentProviderService.getAllEntries().forEach(e ->
            portalComponentsByWorkspace.get(e.getKey()).putAll(e.getValue())
        );
        portalItemComponentProviderService.observableMapProperty().addListener((MapChangeListener<Workspace, ObservableMap<UUID, PortalItem>>) change -> {
            if (change.wasAdded()) {
                change.getValueAdded().addListener((MapChangeListener<UUID, PortalItem>) change1 -> {
                    if (change1.wasAdded()) {
                        portalComponentsByWorkspace.get(change.getKey()).put(change1.getKey(), change1.getValueAdded());
                    } else if (change1.wasRemoved()) {
                        portalComponentsByWorkspace.get(change.getKey()).remove(change1.getKey());
                    }
                });
            } else if (change.wasRemoved()) {
                // remove components from removed workspace
                change.getValueRemoved().forEach((key, value) -> portalComponentsByWorkspace.get(change.getKey()).remove(key));
            }
        });
    }

    /**
     * Unsubscribe from a provider by removing all references to the provider's components.
     * 
     * @param portalItemComponentProviderService portal item component provider service to unsubscribe
     */
    private void unsubscribeFromProvider(PortalItemComponentProviderService portalItemComponentProviderService) {
        portalItemComponentProviderService.getAllEntries().forEach(entry ->
            entry.getValue().forEach((key, value) -> portalComponentsByWorkspace.get(entry.getKey()).remove(key))
        );
    }
    
    @Override
    public ObservableMap<UUID, PortalItem> get(Workspace workspace) {
        // create a map for this workspace to track components if it does not already exist
        portalComponentsByWorkspace.computeIfAbsent(workspace, k -> {
            ObservableMap<UUID, PortalItem> components = FXCollections.observableHashMap();
            portalItemComponentProviderServices.stream()
                .map(portalItemComponentProviderService -> portalItemComponentProviderService.get(workspace))
                .forEach(components::putAll);
            return components;
        });
        // get the component map for the workspace
        return portalComponentsByWorkspace.get(workspace);
    }

    @Override
    public ReadOnlyMapProperty<Workspace, ObservableMap<UUID, PortalItem>> observableMapProperty() {
        return portalComponentsByWorkspace.getReadOnlyProperty();
    }

    @Override
    public Set<Map.Entry<Workspace, ObservableMap<UUID, PortalItem>>> getAllEntries() {
        return portalComponentsByWorkspace.entrySet();
    }
}
