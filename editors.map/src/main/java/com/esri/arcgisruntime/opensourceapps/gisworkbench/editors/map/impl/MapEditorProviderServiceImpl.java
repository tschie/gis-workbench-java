package com.esri.arcgisruntime.opensourceapps.gisworkbench.editors.map.impl;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service.Editor;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service.EditorProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.portal.PortalItem;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true)
public class MapEditorProviderServiceImpl implements EditorProviderService {

    @Override
    public String getName() {
        return "Map";
    }

    @Override
    public Editor create(Workspace workspace, Object data) {
        if (supports(data)) {
            return new MapEditor(workspace, null, data);
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
        return o instanceof ArcGISMap || (o instanceof PortalItem && ((PortalItem) o).getType() == PortalItem.Type.WEBMAP);
    }
}
