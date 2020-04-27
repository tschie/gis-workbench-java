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

package com.esri.arcgisruntime.opensourceapps.workbench.canvas;

import com.esri.arcgisruntime.opensourceapps.workbench.menu.service.MenuService;
import com.esri.arcgisruntime.opensourceapps.workbench.perspective.service.PerspectiveService;
import com.esri.arcgisruntime.opensourceapps.workbench.stage.service.StageService;
import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.util.Arrays;

@Component(immediate = true)
public class Canvas {

    @Reference
    private StageService stageService;

    @Reference
    private PerspectiveService perspectiveService;

    @Reference
    private MenuService menuService;

    @Inject
    private FXMLLoader fxmlLoader;

    @Activate
    private void activate() {
        if (stageService.getStage() != null) {
            setScene(stageService.getStage());
        } else {
            stageService.stageProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    setScene(newValue);
                }
            });
        }
    }

    private void setScene(Stage stage) {
        GuiceContext context = new GuiceContext(this, () -> Arrays.asList(
                new MenuServiceModule(menuService),
                new PerspectiveServiceModule(perspectiveService)
        ));
        context.init();
        fxmlLoader.setClassLoader(Activator.class.getClassLoader());
        fxmlLoader.setLocation(FrameworkUtil.getBundle(Activator.class).getResource("/fxml/main.fxml"));
        try {
            Parent root = fxmlLoader.load();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
