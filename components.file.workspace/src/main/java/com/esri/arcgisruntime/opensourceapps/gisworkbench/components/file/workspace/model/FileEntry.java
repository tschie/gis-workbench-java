package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.workspace.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;

@XmlRootElement
public class FileEntry {
  @XmlAttribute
  private final UUID id;
  @XmlAttribute
  private final String path;

  public FileEntry() {
    this(null, null);
  }

  public FileEntry(UUID id, String path) {
    this.id = id;
    this.path = path;
  }

  public UUID getId() {
    return id;
  }

  public String getPath() {
    return path;
  }
}
