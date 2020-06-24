package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.workspace.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class PortalItems {
  @XmlElement(name = "Entry")
  @XmlElementWrapper(name = "entries")
  private final List<PortalItemEntry> entries;

  public PortalItems() {
    this(new ArrayList<>());
  }

  public PortalItems(List<PortalItemEntry> entries) {
    this.entries = entries;
  }

  public List<PortalItemEntry> getEntries() {
    return entries;
  }
}
