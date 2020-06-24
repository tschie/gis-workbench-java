package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.workspace.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class FileEntries {
  @XmlElement(name = "Entry")
  @XmlElementWrapper(name = "entries")
  private final List<FileEntry> entries;

  public FileEntries() {
    this(new ArrayList<>());
  }

  public FileEntries(List<FileEntry> entries) {
    this.entries = entries;
  }

  public List<FileEntry> getEntries() {
    return entries;
  }
}
