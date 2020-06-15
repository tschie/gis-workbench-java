package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.Activator;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.WorkspaceService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

import java.io.File;
import java.util.Arrays;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

@Component(immediate = true)
public class WorkspaceServiceImpl implements WorkspaceService {

    private final ReadOnlyListWrapper<Workspace> activeWorkspaces =
            new ReadOnlyListWrapper<>(FXCollections.observableArrayList());
    private final ReadOnlyListWrapper<Workspace> recentWorkspaces =
            new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

    @Activate
    private void activate() {
        Preferences preferences = Preferences.userRoot().node(Activator.class.getName());

        // open last active workspaces
        String lastWorkspacesString = preferences.get("lastWorkspaces", "");
        Arrays.stream(lastWorkspacesString.split(";"))
                .map(File::new)
                .filter(File::exists)
                .map(this::workspaceFromFile)
                .forEach(this::open);

        // initialize recent workspaces from preferences
        String recentWorkspacesString = preferences.get("recentWorkspaces", "");
        recentWorkspaces.addAll(Arrays.stream(recentWorkspacesString.split(";"))
                .map(File::new)
                .filter(File::exists)
                .map(this::workspaceFromFile)
                .collect(Collectors.toList()));

        activeWorkspaces.addListener((ListChangeListener<Workspace>) change -> {
            while (change.next()) {
                preferences.put("lastWorkspaces",
                        change.getList().stream()
                                .map(w -> w.getRootDirectory().getAbsolutePath())
                                .collect(Collectors.joining(";"))
                );
                System.out.println(preferences.get("lastWorkspaces", ""));
                if (change.wasAdded()) {
                    recentWorkspaces.addAll(change.getAddedSubList());
                }
            }
        });

        recentWorkspaces.addListener((ListChangeListener<Workspace>) change -> {
            while (change.next()) {
                preferences.put("recentWorkspaces",
                        change.getList().stream()
                                .map(w -> w.getRootDirectory().getAbsolutePath())
                                .limit(20)
                                .collect(Collectors.joining(";"))
                );
            }
        });
    }

    private Workspace workspaceFromFile(File file) {
        return new Workspace(file, file.toPath().resolve(".gisworkbench").toFile());
    }

    @Override
    public void open(Workspace workspace) {
        if (workspace.getRootDirectory().exists()) {
            activeWorkspaces.add(workspace);
        } else {
            throw new IllegalArgumentException("Workspace does not exist");
        }
    }

    @Override
    public void close(Workspace workspace) {
        activeWorkspaces.remove(workspace);
    }

    @Override
    public ReadOnlyListProperty<Workspace> activeWorkspacesProperty() {
        return activeWorkspaces.getReadOnlyProperty();
    }

    @Override
    public ReadOnlyListProperty<Workspace> recentWorkspacesProperty() {
        return recentWorkspaces.getReadOnlyProperty();
    }
}
