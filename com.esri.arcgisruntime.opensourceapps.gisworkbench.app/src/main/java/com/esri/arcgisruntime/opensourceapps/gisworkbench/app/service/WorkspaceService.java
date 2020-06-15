package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.collections.ObservableList;

public interface WorkspaceService {
    void open(Workspace workspace);

    void close(Workspace workspace);

    ReadOnlyListProperty<Workspace> activeWorkspacesProperty();

    default ObservableList<Workspace> getActiveWorkspaces() {
        return activeWorkspacesProperty().get();
    }

    ReadOnlyListProperty<Workspace> recentWorkspacesProperty();

    default ObservableList<Workspace> getRecentWorkspaces() {
        return recentWorkspacesProperty().get();
    };
}
