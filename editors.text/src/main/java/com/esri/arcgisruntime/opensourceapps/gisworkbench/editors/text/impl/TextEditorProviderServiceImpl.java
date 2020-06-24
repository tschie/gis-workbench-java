package com.esri.arcgisruntime.opensourceapps.gisworkbench.editors.text.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.file.service.FileComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service.Editor;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service.EditorProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.MapChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.File;
import java.util.UUID;

@Component(immediate = true)
public class TextEditorProviderServiceImpl implements EditorProviderService {

    @Reference
    private FileComponentService fileComponentService;

    @Override
    public String getName() {
        return "Text";
    }

    @Override
    public boolean supports(Workspace workspace, UUID uuid) {
        File file = fileComponentService.get(workspace).get(uuid);
        return file != null && file.getName().endsWith(".txt");
    }

    @Override
    public ReadOnlyBooleanProperty supportsProperty(Workspace workspace, UUID uuid) {
        ReadOnlyBooleanWrapper supported = new ReadOnlyBooleanWrapper(supports(workspace, uuid));
        fileComponentService.get(workspace).addListener((MapChangeListener<UUID, File>) change ->
            supported.set(change.getMap().containsKey(uuid))
        );
        return supported.getReadOnlyProperty();
    }

    @Override
    public Editor create(Workspace workspace, UUID uuid) {
        File file = fileComponentService.get(workspace).get(uuid);
        return file != null ? new TextEditor(workspace, file, uuid) : null;
    }
}
