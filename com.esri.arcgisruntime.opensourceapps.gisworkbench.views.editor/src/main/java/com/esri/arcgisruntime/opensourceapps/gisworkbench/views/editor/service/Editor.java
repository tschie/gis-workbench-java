package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.scene.Node;

public abstract class Editor {

    private final Workspace workspace;
    private final String id;
    private final Object data;

    public Editor(Workspace workspace, String id, Object data) {
        this.workspace = workspace;
        this.id = id;
        this.data = data;
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public String getId() {
        return id;
    }

    public abstract Node getNode();

    public abstract String getDisplayName();
}
