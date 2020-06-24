package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.portalitems.service;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.repository.ObservableMapRepository;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.collections.ObservableMap;

import java.util.UUID;

public interface PortalItemMapComponentService extends ObservableMapRepository<Workspace, ObservableMap<UUID, ArcGISMap>> {
}
