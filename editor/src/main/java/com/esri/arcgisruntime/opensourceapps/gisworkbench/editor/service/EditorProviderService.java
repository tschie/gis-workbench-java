package com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.beans.property.ReadOnlyBooleanProperty;

import java.util.UUID;

public interface EditorProviderService {
  String getName();
  boolean supports(Workspace workspace, UUID id);
  ReadOnlyBooleanProperty supportsProperty(Workspace workspace, UUID id);
  Editor create(Workspace workspace, UUID id);
}
