package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.workspace.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.workspace.service.WorkspacePortalItemComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.service.PortalItemComponentProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.collections.ObservableMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.UUID;

@Component(immediate = true)
public class WorkspacePortalItemComponentProviderServiceImpl implements PortalItemComponentProviderService {

  @Reference
  private WorkspacePortalItemComponentService workspacePortalItemComponentService;

  @Override
  public ReadOnlyMapProperty<Workspace, ObservableMap<UUID, PortalItem>> observableMapProperty() {
    return workspacePortalItemComponentService.observableMapProperty();
  }

  @Override
  public ObservableMap<UUID, PortalItem> get(Workspace workspace) {
    return workspacePortalItemComponentService.get(workspace);
  }
}
