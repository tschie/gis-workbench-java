package com.esri.arcgisruntime.opensourceapps.gisworkbench.editors.text.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.editors.text.view.TextDisplay;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service.Editor;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service.EditorProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import org.osgi.service.component.annotations.Component;

import java.io.File;

@Component(immediate = true)
public class TextEditorProviderServiceImpl implements EditorProviderService {

    @Override
    public String getName() {
        return "Text";
    }

    @Override
    public Editor create(Workspace workspace, String id, Object data) {
        if (data instanceof File) {
            return new TextEditor(workspace, id, new TextDisplay((File) data));
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Object o) {
        return false;
    }
}
