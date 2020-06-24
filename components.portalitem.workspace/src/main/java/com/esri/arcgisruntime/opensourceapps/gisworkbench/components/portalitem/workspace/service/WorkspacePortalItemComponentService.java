package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.workspace.service;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.repository.ObservableMapRepository;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.collections.ObservableMap;

import java.util.UUID;

public interface WorkspacePortalItemComponentService extends ObservableMapRepository<Workspace, ObservableMap<UUID,
    PortalItem>> {
}
