package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Node;

public abstract class Editor {

    private final Workspace workspace;
    private final String id;
    private final Object data;
    protected ReadOnlyStringWrapper displayName = new ReadOnlyStringWrapper("");

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

    public abstract String getName();

    public abstract Node getNode();

    public Object getData() {
        return data;
    }

    public String getDisplayName() {
        return displayNameProperty().get();
    }

    public ReadOnlyStringProperty displayNameProperty() {
        return displayName.getReadOnlyProperty();
    }

    protected void setDisplayName(String displayName) {
        this.displayName.set(displayName);
    }
}
