/*
 * Copyright 2020 Esri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this portal except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.maps.view;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.events.editor.OpenEditorEvent;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.maps.Activator;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.maps.model.MapEntity;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import org.osgi.framework.FrameworkUtil;

import java.io.IOException;

public class MapListCell extends ListCell<MapEntity> {

    private final Workspace workspace;
    private final EventService eventService;

    public MapListCell(Workspace workspace, EventService eventService) {
        this.workspace = workspace;
        this.eventService = eventService;
    }

    @Override
    protected void updateItem(MapEntity mapEntity, boolean b) {
        super.updateItem(mapEntity, b);
        if (!b && mapEntity != null) {
            ArcGISMap map = mapEntity.getMap();
            if (map.getLoadStatus() == LoadStatus.LOADED && map.getItem() != null) {
                setText(map.getItem().getTitle());
            }
            ImageView imageView = new ImageView();
            setGraphic(imageView);
            try {
                imageView.setImage(new Image(FrameworkUtil.getBundle(Activator.class).getResource(
                    "/images/map.png").openStream(), 14, 14, true, true));
            } catch (IOException ex) {
                // do nothing
            }

            setOnMouseClicked(mouseEvent -> {
                if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                    eventService.emit(new OpenEditorEvent(mapEntity.getId(), workspace));
                }
            });
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}
