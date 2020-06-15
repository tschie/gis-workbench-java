/*
 * Copyright 2020 Esri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
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

package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.controller;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspective.service.PerspectiveProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspective.service.PerspectiveService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.view.Workbench;
import com.google.inject.Inject;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class WorkbenchController {

    @Inject
    private Workbench workbench;

    @Inject
    private PerspectiveService perspectiveService;

    @FXML
    private StackPane stackPane;

    @FXML
    private void initialize() {
        // change the perspective when the provider changes
        workbench.perspectiveProviderServiceProperty().addListener((observable, oldValue, newValue) -> {
            stackPane.getChildren().clear();
            if (newValue != null) {
                Platform.runLater(() -> stackPane.getChildren().add(newValue.createNodeForWorkspace(workbench.getWorkspace())));
            }
        });

        if (workbench.getPerspectiveProviderService() != null) {
            // set the perspective if a provider is set
            Platform.runLater(() -> stackPane.getChildren().add(workbench.getPerspectiveProviderService().createNodeForWorkspace(workbench.getWorkspace())));
        } else {
            // if no provider is set, set the first available provider
            perspectiveService.getAll().stream()
                    .filter(perspectiveProviderService -> perspectiveProviderService.isAvailableForWorkspace(workbench.getWorkspace()))
                    .findFirst()
                    .ifPresent(workbench::setPerspectiveProviderService);
        }

        // if new perspective providers are added and a provider is not yet set, set the first available provider
        perspectiveService.observableListProperty().addListener((ListChangeListener<PerspectiveProviderService>) change -> {
            while (change.next()) {
                if (change.wasAdded() && workbench.getPerspectiveProviderService() == null) {
                    change.getAddedSubList().stream()
                            .filter(perspectiveProviderService -> perspectiveProviderService.isAvailableForWorkspace(workbench.getWorkspace()))
                            .findFirst()
                            .ifPresent(workbench::setPerspectiveProviderService);
                }
            }
        });
    }
}
