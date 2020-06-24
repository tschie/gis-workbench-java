package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.maps.model;

import com.esri.arcgisruntime.mapping.ArcGISMap;

import java.util.UUID;

public class MapEntity {
  private final UUID id;
  private final ArcGISMap map;

  public MapEntity(UUID id, ArcGISMap map) {
    this.id = id;
    this.map = map;
  }

  public UUID getId() {
    return id;
  }

  public ArcGISMap getMap() {
    return map;
  }
}
