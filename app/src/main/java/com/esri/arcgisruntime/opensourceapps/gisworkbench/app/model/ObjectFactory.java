package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.model;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.Perspective;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public Layout createLayout() {
        return new Layout();
    }

    public Perspective createPerspective() {
        return new Perspective();
    }
}
