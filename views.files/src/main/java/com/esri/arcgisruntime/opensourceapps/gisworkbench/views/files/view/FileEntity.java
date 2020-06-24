package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.files.view;

import java.io.File;
import java.util.UUID;

public class FileEntity {
  private final UUID id;
  private final File file;

  public FileEntity(UUID id, File file) {
    this.id = id;
    this.file = file;
  }

  public UUID getId() {
    return id;
  }

  public File getFile() {
    return file;
  }
}
