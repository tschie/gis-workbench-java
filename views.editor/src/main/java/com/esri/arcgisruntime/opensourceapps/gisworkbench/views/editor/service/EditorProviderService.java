package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;

/**
 * Defines an editor provider.
 */
public interface EditorProviderService {
    /**
     * The name of the editor
     * @return
     */
    String getName();
    Editor create(Workspace workspace, Object data);
    Editor recreate(Workspace workspace, String id);
    boolean supports(Object object);
}
