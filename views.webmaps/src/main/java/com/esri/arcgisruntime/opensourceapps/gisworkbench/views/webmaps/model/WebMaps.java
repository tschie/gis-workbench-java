package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "webMap")
public class WebMaps {
    @XmlElement @XmlElementWrapper(name = "webMaps")
    private final List<WebMap> webMaps;

    public WebMaps() {
        this(new ArrayList<>());
    }

    public WebMaps(List<WebMap> webMaps) {
        this.webMaps = webMaps;
    }

    public List<WebMap> getWebMaps() {
        return webMaps;
    }
}
