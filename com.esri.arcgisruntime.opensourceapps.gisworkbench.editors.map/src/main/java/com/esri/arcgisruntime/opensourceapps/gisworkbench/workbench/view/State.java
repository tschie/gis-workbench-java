package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.view;

import javax.xml.bind.annotation.*;

@XmlRootElement(name = "Window")
@XmlAccessorType(XmlAccessType.FIELD)
public class State {

    @XmlAttribute
    private final Double x;

    @XmlAttribute
    private final Double y;

    @XmlAttribute
    private final Double width;

    @XmlAttribute
    private final Double height;

    @XmlAttribute
    private final Boolean maximized;

    public State() {
        this(0.0, 0.0, 0.0, 0.0, false);
    }

    public State(Double x, Double y, Double width, Double height, Boolean maximized) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maximized = maximized;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getWidth() {
        return width;
    }

    public Double getHeight() {
        return height;
    }

    public Boolean getMaximized() {
        return maximized;
    }
}
