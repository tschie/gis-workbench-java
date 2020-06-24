package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@XmlRootElement
public class Editors {
  @XmlElement
  @XmlElementWrapper(name = "ids")
  private final List<UUID> ids;

  public Editors() {
    this(new ArrayList<>());
  }

  public Editors(List<UUID> ids) {
    this.ids = ids;
  }

  public List<UUID> getIds() {
    return ids;
  }
}
