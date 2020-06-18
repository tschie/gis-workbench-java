package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WebMap {
    @XmlAttribute
    private final String url;
    @XmlAttribute
    private final String id;

    public WebMap() {
        this("", "");
    }

    public WebMap(String url, String id) {
        this.url = url;
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }
}
