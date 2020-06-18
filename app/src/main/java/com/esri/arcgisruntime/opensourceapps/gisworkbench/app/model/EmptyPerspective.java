package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.model;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.Perspective;

import java.util.ArrayList;

public class EmptyPerspective extends Perspective {
    public EmptyPerspective() {
        super("Empty", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}
