package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.workspace.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.service.FileComponentProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.workspace.service.WorkspaceFileComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.collections.ObservableMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.File;
import java.util.UUID;

@Component(immediate = true)
public class WorkspaceFileComponentProviderServiceImpl implements FileComponentProviderService {

  @Reference
  private WorkspaceFileComponentService workspaceFileComponentService;

  @Override
  public ReadOnlyMapProperty<Workspace, ObservableMap<UUID, File>> observableMapProperty() {
    return workspaceFileComponentService.observableMapProperty();
  }

  @Override
  public ObservableMap<UUID, File> get(Workspace workspace) {
    return workspaceFileComponentService.get(workspace);
  }
}
