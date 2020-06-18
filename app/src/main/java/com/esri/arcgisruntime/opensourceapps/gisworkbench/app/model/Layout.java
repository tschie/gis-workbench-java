package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.model;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.Perspective;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Layout {

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

    @XmlElement @XmlElementWrapper(name = "perspective")
    private final List<Perspective> perspectives;

    @XmlAttribute
    private final String perspectiveName;

    public Layout() {
        this(0.0, 0.0, 600.0, 400.0, false, new ArrayList<>(Collections.singletonList(new EmptyPerspective())), "Empty");
    }

    public Layout(
            Double x,
            Double y,
            Double width,
            Double height,
            Boolean maximized,
            List<Perspective> perspectives,
            String perspectiveName
    ) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maximized = maximized;
        this.perspectives = perspectives;
        this.perspectiveName = perspectiveName;
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

    public List<Perspective> getPerspectives() {
        return perspectives;
    }

    public String getPerspectiveName() {
        return perspectiveName;
    }
}
