package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.portalitem.workspace.model;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

  public PortalItems createPortalItems() {
    return new PortalItems();
  }

  public PortalItemEntry createPortalItemEntries() {
    return new PortalItemEntry();
  }
}
