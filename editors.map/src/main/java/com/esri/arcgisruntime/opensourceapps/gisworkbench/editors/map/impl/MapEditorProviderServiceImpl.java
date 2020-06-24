package com.esri.arcgisruntime.opensourceapps.gisworkbench.editors.map.impl;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.components.map.service.MapComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service.Editor;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service.EditorProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.collections.MapChangeListener;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.UUID;

@Component(immediate = true)
public class MapEditorProviderServiceImpl implements EditorProviderService {

    @Reference
    private MapComponentService mapComponentService;

    @Override
    public String getName() {
        return "Maps";
    }

    @Override
    public boolean supports(Workspace workspace, UUID uuid) {
        ArcGISMap map = mapComponentService.get(workspace).get(uuid);
        return map != null;
    }

    @Override
    public ReadOnlyBooleanProperty supportsProperty(Workspace workspace, UUID uuid) {
        ReadOnlyBooleanWrapper supported = new ReadOnlyBooleanWrapper(supports(workspace, uuid));
        // TODO: make value type a property for components
        mapComponentService.get(workspace).addListener((MapChangeListener<UUID, ArcGISMap>) change ->
            supported.set(change.getMap().containsKey(uuid))
        );
        return supported.getReadOnlyProperty();
    }

    @Override
    public Editor create(Workspace workspace, UUID uuid) {
        ArcGISMap map = mapComponentService.get(workspace).get(uuid);
        return map != null ? new MapEditor(workspace, map, uuid) : null;
    }
}
