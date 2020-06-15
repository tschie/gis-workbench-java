package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;

public class Context {

    private final File workspace;
    private final Stage stage;
    private final ObjectProperty<Object> selection = new SimpleObjectProperty<>();

    public Context(File workspace, Stage stage) {
        this.workspace = workspace;
        this.stage = stage;
    }

    public Object getSelection() {
        return selection.get();
    }

    public ObjectProperty<Object> selectionProperty() {
        return selection;
    }

    public void setSelection(Object selection) {
        this.selection.set(selection);
    }

    public File getWorkspace() {
        return workspace;
    }

    public Stage getStage() {
        return stage;
    }

    public Window getWindow() {
        return stage.getOwner();
    }
}
