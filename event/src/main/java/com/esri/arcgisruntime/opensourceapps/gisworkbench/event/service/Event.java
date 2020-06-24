package com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;

public class Event<T> {
    private final T payload;
    private final Workspace workspace;

    public Event(T payload, Workspace workspace) {
        this.payload = payload;
        this.workspace = workspace;
    }

    public T getPayload() {
        return payload;
    }

    public Workspace getWorkspace() {
        return workspace;
    }
}
