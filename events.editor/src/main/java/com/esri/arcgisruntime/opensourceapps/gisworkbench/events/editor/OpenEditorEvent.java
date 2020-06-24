package com.esri.arcgisruntime.opensourceapps.gisworkbench.events.editor;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.Event;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;

import java.util.UUID;

public class OpenEditorEvent extends Event<UUID> {
  public OpenEditorEvent(UUID id, Workspace workspace) {
    super(id, workspace);
  }
}
