package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.workspace.service;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.repository.ObservableMapRepository;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.collections.ObservableMap;

import java.io.File;
import java.util.UUID;

public interface WorkspaceFileComponentService extends ObservableMapRepository<Workspace, ObservableMap<UUID, File>> {
}
