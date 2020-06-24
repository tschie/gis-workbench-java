package com.esri.arcgisruntime.opensourceapps.gisworkbench.editors.text.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service.Editor;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.editors.text.view.TextDisplay;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;

import java.io.File;
import java.util.UUID;

public class TextEditor extends Editor {

    private final File file;

    public TextEditor(Workspace workspace, File file, UUID id) {
        //TODO: serialization for editor properties such as scroll position
        super(workspace, id,"Text", new TextDisplay(file));
        this.file = file;
        setDisplayText(file.getName());
    }

    public File getFile() {
        return file;
    }
}
