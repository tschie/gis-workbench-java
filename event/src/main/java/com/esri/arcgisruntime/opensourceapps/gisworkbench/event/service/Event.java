package com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;

import java.util.List;

public interface Event {
    List<String> getEventTypes();
    Workspace getWorkspace();
    Object getData();
}
