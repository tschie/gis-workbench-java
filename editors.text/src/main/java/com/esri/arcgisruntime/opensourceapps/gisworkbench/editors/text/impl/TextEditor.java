package com.esri.arcgisruntime.opensourceapps.gisworkbench.editors.text.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.editors.text.view.TextDisplay;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service.Editor;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.scene.Node;

import java.io.File;

public class TextEditor extends Editor {

    private final Node node;

    public TextEditor(Workspace workspace, String id, Object data) {
        super(workspace, id, data);
        this.node = new TextDisplay((File) data);
    }

    @Override
    public Node getNode() {
        return node;
    }

    @Override
    public String getDisplayName() {
        return "File";
    }
}
