package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.model;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public WebMaps createWebMaps() {
        return new WebMaps();
    }

    public WebMap createWebMap() {
        return new WebMap();
    }
}
