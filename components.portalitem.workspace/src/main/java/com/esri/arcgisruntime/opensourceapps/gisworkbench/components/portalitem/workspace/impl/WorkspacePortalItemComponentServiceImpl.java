package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.workspace.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.workspace.model.PortalItemEntry;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.workspace.model.PortalItems;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.workspace.service.WorkspacePortalItemComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.xml.XmlFileSerializer;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.beans.property.ReadOnlyMapWrapper;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component(immediate = true)
public class WorkspacePortalItemComponentServiceImpl implements WorkspacePortalItemComponentService {

  private final ReadOnlyMapWrapper<Workspace, ObservableMap<UUID, PortalItem>> portalComponentsByWorkspace =
      new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());

  private final XmlFileSerializer xmlFileSerializer = new XmlFileSerializer();

  @Activate
  private void activate() {
    portalComponentsByWorkspace.addListener((MapChangeListener<Workspace, ObservableMap<UUID, PortalItem>>) change -> {
      if (change.wasAdded()) {
        change.getValueAdded().addListener((MapChangeListener<UUID, PortalItem>) change1 -> serialize(change.getKey()));
      }
    });
  }

  @Override
  public ObservableMap<UUID, PortalItem> get(Workspace workspace) {
    portalComponentsByWorkspace.computeIfAbsent(workspace, k -> {
      ObservableMap<UUID, PortalItem> components = FXCollections.observableHashMap();
      PortalItems portalItems = deserialize(workspace);
      if (portalItems != null) {
        portalItems.getEntries().forEach(e ->
            components.put(e.getId(), new PortalItem(new Portal(e.getPortalUrl()), e.getItemId()))
        );
      }
      return components;
    });
    return portalComponentsByWorkspace.get(workspace);
  }

  @Override
  public ReadOnlyMapProperty<Workspace, ObservableMap<UUID, PortalItem>> observableMapProperty() {
    return portalComponentsByWorkspace.getReadOnlyProperty();
  }

  @Override
  public Set<Map.Entry<Workspace, ObservableMap<UUID, PortalItem>>> getAllEntries() {
    return observableMapProperty().entrySet();
  }

  private File getXmlFile(Workspace workspace) {
    return workspace.getMetadataDirectory().toPath().resolve(getClass().getPackageName() + ".xml").toFile();
  }

  private PortalItems deserialize(Workspace workspace) {
    File xmlFile = getXmlFile(workspace);
    if (xmlFile.exists()) {
      return xmlFileSerializer.deserialize(xmlFile, PortalItems.class);
    }
    return null;
  }

  private void serialize(Workspace workspace) {
    File xmlFile = getXmlFile(workspace);
    // create file if it does not exist
    if (!xmlFile.exists()) {
      try {
        if (!xmlFile.getParentFile().exists()) {
          Files.createDirectory(xmlFile.getParentFile().toPath());
        }
        Files.createFile(xmlFile.toPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    // serialize state to file
    if (xmlFile.exists()) {
      portalComponentsByWorkspace.get(workspace).forEach((key, value) -> {
        System.out.println(key);
        System.out.println(value.getPortal().getUri());
        System.out.println(value.getItemId());
      });
      PortalItems portalItems = new PortalItems(portalComponentsByWorkspace.get(workspace).entrySet().stream()
          .map(e -> new PortalItemEntry(e.getKey(), e.getValue().getPortal().getUri(), e.getValue().getItemId()))
          .collect(Collectors.toList()));
      xmlFileSerializer.serialize(xmlFile, portalItems);
    }
  }
}
