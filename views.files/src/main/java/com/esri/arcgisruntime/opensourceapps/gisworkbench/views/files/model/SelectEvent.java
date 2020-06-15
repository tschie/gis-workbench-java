package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.files.model;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.Event;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;

import java.io.File;
import java.util.List;

public class SelectEvent implements Event {

    private final File file;
    private final Workspace workspace;

    public SelectEvent(File file, Workspace workspace) {
        this.file = file;
        this.workspace = workspace;
    }

    public File getFile() {
        return file;
    }

    @Override
    public Workspace getWorkspace() {
        return workspace;
    }

    @Override
    public List<String> getEventTypes() {
        return List.of("select", "click", getClass().getName());
    }

    @Override
    public Object getData() {
        return getFile();
    }
}
