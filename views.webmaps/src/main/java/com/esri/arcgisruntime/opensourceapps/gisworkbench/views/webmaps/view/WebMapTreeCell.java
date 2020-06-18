/*
 * Copyright 2020 Esri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this map except in compliance with the License. You may obtain a copy of
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

package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.view;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.Activator;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.webmaps.model.OpenEvent;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import org.osgi.framework.FrameworkUtil;

import java.io.IOException;

public class WebMapTreeCell extends TreeCell<ArcGISMap> {

    public WebMapTreeCell(Workspace workspace, EventService eventService) {
        setOnMouseClicked(mouseEvent -> {
            if (
                getTreeItem() != null &&
                getTreeItem().getValue() != null &&
                mouseEvent.getButton() == MouseButton.PRIMARY &&
                mouseEvent.getClickCount() == 2
            ) {
                eventService.emit(new OpenEvent(getTreeItem().getValue(), workspace));
            }
        });
    }

    @Override
    protected void updateItem(ArcGISMap map, boolean b) {
        super.updateItem(map, b);
        if (!b && map != null) {
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
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}
