package com.esri.arcgisruntime.opensourceapps.gisworkbench.editors.text.impl;

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
    public Editor create(Workspace workspace, Object data) {
        if (data instanceof File) {
            return new TextEditor(workspace, null, data);
        } else {
            return null;
        }
    }

    @Override
    public Editor recreate(Workspace workspace, String id) {
        return null;
    }

    @Override
    public boolean supports(Object o) {
        return o instanceof File && ((File) o).getName().endsWith(".txt");
    }
}
