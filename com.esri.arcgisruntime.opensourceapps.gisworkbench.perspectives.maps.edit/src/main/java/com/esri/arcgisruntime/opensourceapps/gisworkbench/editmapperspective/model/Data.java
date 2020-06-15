package com.esri.arcgisruntime.opensourceapps.gisworkbench.editmapperspective.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Data {
    @XmlAttribute
    private final String text;

    public Data() {
        this("");
    }

    public Data(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
