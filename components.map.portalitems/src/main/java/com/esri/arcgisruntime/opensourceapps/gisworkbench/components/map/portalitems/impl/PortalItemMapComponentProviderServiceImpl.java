package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.portalitems.impl;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.portalitems.service.PortalItemMapComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.service.MapComponentProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.service.MapComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.service.PortalItemComponentProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.beans.property.ReadOnlyMapProperty;
import javafx.collections.ObservableMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.UUID;

@Component(immediate = true)
public class PortalItemMapComponentProviderServiceImpl implements MapComponentProviderService {

  @Reference
  private PortalItemMapComponentService portalItemMapComponentService;

  @Override
  public ReadOnlyMapProperty<Workspace, ObservableMap<UUID, ArcGISMap>> observableMapProperty() {
    return portalItemMapComponentService.observableMapProperty();
  }

  @Override
  public ObservableMap<UUID, ArcGISMap> get(Workspace workspace) {
    return portalItemMapComponentService.get(workspace);
  }
}
