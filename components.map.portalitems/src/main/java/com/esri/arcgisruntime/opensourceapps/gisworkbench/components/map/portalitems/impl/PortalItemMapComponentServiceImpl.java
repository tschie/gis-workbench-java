package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.portalitems.impl;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.portalitems.service.PortalItemMapComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.service.PortalItemComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Component(immediate = true)
public class PortalItemMapComponentServiceImpl implements PortalItemMapComponentService {

  @Reference
  private PortalItemComponentService portalItemComponentService;

  private final ReadOnlyMapWrapper<Workspace, ObservableMap<UUID, ArcGISMap>> mapComponentsByWorkspace =
      new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());

  @Activate
  private void activate() {
    // copy all portal item components which are webmaps to map components
    portalItemComponentService.getAllEntries().forEach(entry -> {
      mapComponentsByWorkspace.computeIfAbsent(entry.getKey(), k -> FXCollections.observableHashMap());
      entry.getValue().forEach((key, portalItem) -> {
        // add after loading (assuming order doesn't matter)
        portalItem.loadAsync();
        portalItem.addDoneLoadingListener(() -> {
          if (portalItem.getLoadStatus() == LoadStatus.LOADED && portalItem.getType() == PortalItem.Type.WEBMAP) {
            mapComponentsByWorkspace.get(entry.getKey()).put(key, new ArcGISMap(portalItem));
          }
        });
      });
    });

    // watch for changes to portal items
    portalItemComponentService.observableMapProperty().addListener((MapChangeListener<Workspace, ObservableMap<UUID, PortalItem>>) change -> {
      mapComponentsByWorkspace.computeIfAbsent(change.getKey(), k -> FXCollections.observableHashMap());
      if (change.wasAdded()) {
        // add components
        change.getValueAdded().forEach((key, portalItem) -> {
          // add after loading (assuming order doesn't matter)
          portalItem.loadAsync();
          portalItem.addDoneLoadingListener(() -> {
            if (portalItem.getLoadStatus() == LoadStatus.LOADED && portalItem.getType() == PortalItem.Type.WEBMAP) {
              System.out.println(portalItem);
              mapComponentsByWorkspace.get(change.getKey()).put(key, new ArcGISMap(portalItem));
            }
          });
        });
        // subscribe
        change.getValueAdded().addListener((MapChangeListener<UUID, PortalItem>) change1 -> {
          if (change1.wasAdded()) {
            PortalItem portalItem = change1.getValueAdded();
            portalItem.loadAsync();
            portalItem.addDoneLoadingListener(() -> {
              if (portalItem.getLoadStatus() == LoadStatus.LOADED && portalItem.getType() == PortalItem.Type.WEBMAP) {
                mapComponentsByWorkspace.get(change.getKey()).put(change1.getKey(), new ArcGISMap(portalItem));
              }
            });
          } else if (change1.wasRemoved()) {
            mapComponentsByWorkspace.get(change.getKey()).remove(change1.getKey());
          }
        });
      } else if (change.wasRemoved()) {
        change.getValueAdded().forEach((key, portalItem) -> mapComponentsByWorkspace.get(change.getKey()).remove(key));
      }
    });
  }

  @Override
  public ObservableMap<UUID, ArcGISMap> get(Workspace workspace) {
    System.out.println(mapComponentsByWorkspace.get(workspace) == null);
    mapComponentsByWorkspace.computeIfAbsent(workspace, k -> FXCollections.observableHashMap());
    return mapComponentsByWorkspace.get(workspace);
  }

  @Override
  public ReadOnlyMapProperty<Workspace, ObservableMap<UUID, ArcGISMap>> observableMapProperty() {
    return mapComponentsByWorkspace.getReadOnlyProperty();
  }

  @Override
  public Set<Map.Entry<Workspace, ObservableMap<UUID, ArcGISMap>>> getAllEntries() {
    return observableMapProperty().entrySet();
  }
}
