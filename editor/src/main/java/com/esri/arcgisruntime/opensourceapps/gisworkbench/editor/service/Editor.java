package com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;

import java.util.UUID;

public class Editor {

  private final Workspace workspace;
  private final UUID id;
  private final String name;
  private final Node node;
  private final StringProperty displayText;

  public Editor(Workspace workspace, UUID id, String name, Node node) {
    this.workspace = workspace;
    this.id = id;
    this.name = name;
    this.node = node;
    this.displayText = new SimpleStringProperty("");
  }

  public Workspace getWorkspace() {
    return workspace;
  }

  public UUID getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Node getNode() {
    return node;
  }

  public String getDisplayText() {
    return displayText.get();
  }

  public StringProperty displayTextProperty() {
    return this.displayText;
  }

  protected void setDisplayText(String displayText) {
    this.displayText.set(displayText);
  }
}
