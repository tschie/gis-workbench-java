package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;

public interface EditorProviderService {
    String getName();
    Editor create(Workspace workspace, String id, Object data);
    boolean supports(Object object);
}
