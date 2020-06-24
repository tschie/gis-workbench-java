package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.portalitems.model;

import com.esri.arcgisruntime.portal.PortalItem;

import java.util.UUID;

public class PortalItemEntity {
  private final UUID id;
  private final PortalItem portalItem;

  public PortalItemEntity(UUID id, PortalItem portalItem) {
    this.id = id;
    this.portalItem = portalItem;
  }

  public UUID getId() {
    return id;
  }

  public PortalItem getPortalItem() {
    return portalItem;
  }
}
