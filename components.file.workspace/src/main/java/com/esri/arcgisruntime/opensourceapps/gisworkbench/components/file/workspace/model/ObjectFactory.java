package com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.workspace.model;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

  public FileEntries createFileEntries() {
    return new FileEntries();
  }

  public FileEntry createFileEntry() {
    return new FileEntry();
  }
}
