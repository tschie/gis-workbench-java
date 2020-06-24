package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.workspace.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

@XmlRootElement
public class PortalItemEntry {
  @XmlAttribute
  private final UUID id;
  @XmlAttribute
  private final String portalUrl;
  @XmlAttribute
  private final String itemId;

  public PortalItemEntry() {
    this(null, null, null);
  }

  public PortalItemEntry(UUID id, String portalUrl, String itemId) {
    this.id = id;
    this.portalUrl = portalUrl;
    this.itemId = itemId;
  }

  public UUID getId() {
    return id;
  }

  public String getPortalUrl() {
    return portalUrl;
  }

  public String getItemId() {
    return itemId;
  }
}
