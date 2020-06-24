package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.workspace.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.workspace.model.FileEntries;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.workspace.model.FileEntry;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.workspace.service.WorkspaceFileComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.xml.XmlFileSerializer;
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
public class WorkspaceFileComponentServiceImpl implements WorkspaceFileComponentService {

  private final ReadOnlyMapWrapper<Workspace, ObservableMap<UUID, File>> fileComponentsByWorkspace =
      new ReadOnlyMapWrapper<>(FXCollections.observableHashMap());

  private final XmlFileSerializer xmlFileSerializer = new XmlFileSerializer();

  @Activate
  private void activate() {
    fileComponentsByWorkspace.addListener((MapChangeListener<Workspace, ObservableMap<UUID, File>>) change -> {
      if (change.wasAdded()) {
        change.getValueAdded().addListener((MapChangeListener<UUID, File>) change1 -> serialize(change.getKey()));
      }
    });
  }

  @Override
  public ObservableMap<UUID, File> get(Workspace workspace) {
    if (fileComponentsByWorkspace.get(workspace) == null) {
      ObservableMap<UUID, File> components = FXCollections.observableHashMap();
      // deserialize components
      FileEntries fileEntries = deserialize(workspace);
      if (fileEntries != null) {
        fileEntries.getEntries().forEach(e -> {
          File file = new File(e.getPath());
          // remove components which were serialized that no longer exist
          if (file.exists()) {
            components.put(e.getId(), file);
          }
        });
      }
      // create new components from new files
      componentizeRecursively(workspace.getRootDirectory(), components);
      fileComponentsByWorkspace.put(workspace, components);
      serialize(workspace);
    }
    return fileComponentsByWorkspace.get(workspace);
  }

  /**
   * Adds a new component for the given file if it does not already exist as a value in components. Operates
   * recursively on the given file's children.
   *
   * @param file file
   * @param components components
   */
  private void componentizeRecursively(File file, ObservableMap<UUID, File> components) {
    if (file.exists() && !components.containsValue(file)) {
      components.put(UUID.randomUUID(), file);
      File[] files = file.listFiles();
      if (files != null) {
        for (File f : files) {
          componentizeRecursively(f, components);
        }
      }
    }
  }

  @Override
  public ReadOnlyMapProperty<Workspace, ObservableMap<UUID, File>> observableMapProperty() {
    return fileComponentsByWorkspace.getReadOnlyProperty();
  }

  @Override
  public Set<Map.Entry<Workspace, ObservableMap<UUID, File>>> getAllEntries() {
    return observableMapProperty().entrySet();
  }

  private File getXmlFile(Workspace workspace) {
    return workspace.getMetadataDirectory().toPath().resolve(getClass().getPackageName() + ".xml").toFile();
  }

  private FileEntries deserialize(Workspace workspace) {
    File xmlFile = getXmlFile(workspace);
    if (xmlFile.exists()) {
      return xmlFileSerializer.deserialize(xmlFile, FileEntries.class);
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
      FileEntries fileEntries = new FileEntries(fileComponentsByWorkspace.get(workspace).entrySet().stream()
          .map(e -> new FileEntry(e.getKey(), e.getValue().getAbsolutePath()))
          .collect(Collectors.toList()));
      xmlFileSerializer.serialize(xmlFile, fileEntries);
    }
  }
}
