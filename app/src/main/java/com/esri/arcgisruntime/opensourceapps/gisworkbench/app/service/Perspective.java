package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class Perspective {
    @XmlAttribute
    private final String name;
    @XmlElement @XmlElementWrapper(name = "left")
    private final List<String> leftPanels;
    @XmlElement @XmlElementWrapper(name = "right")
    private final List<String> rightPanels;
    @XmlElement @XmlElementWrapper(name = "bottom")
    private final List<String> bottomPanels;

    public Perspective() {
        this("Empty", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    public Perspective(String name, List<String> leftPanels, List<String> rightPanels, List<String> bottomPanels) {
        this.name = name;
        this.leftPanels = leftPanels;
        this.rightPanels = rightPanels;
        this.bottomPanels = bottomPanels;
    }

    public String getName() {
        return name;
    }

    public List<String> getLeftPanels() {
        return leftPanels;
    }

    public List<String> getRightPanels() {
        return rightPanels;
    }

    public List<String> getBottomPanels() {
        return bottomPanels;
    }
}
