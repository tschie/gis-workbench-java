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

package com.esri.arcgisruntime.opensourceapps.workbench.project.impl;

import com.esri.arcgisruntime.opensourceapps.workbench.perspective.service.Perspective;
import com.esri.arcgisruntime.opensourceapps.workbench.perspective.service.PerspectiveProvider;
import com.esri.arcgisruntime.opensourceapps.workbench.project.Activator;
import com.esri.arcgisruntime.opensourceapps.workbench.project.ProjectServiceModule;
import com.esri.arcgisruntime.opensourceapps.workbench.project.SelectionServiceModule;
import com.esri.arcgisruntime.opensourceapps.workbench.project.TabServiceModule;
import com.esri.arcgisruntime.opensourceapps.workbench.project.service.ProjectService;
import com.esri.arcgisruntime.opensourceapps.workbench.project.service.TabService;
import com.esri.arcgisruntime.opensourceapps.workbench.selection.service.SelectionService;
import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.util.Arrays;

@Component(immediate = true)
public class ProjectFilesPerspectiveServiceProviderImpl implements PerspectiveProvider {

    private final ReadOnlyObjectWrapper<Perspective> perspective = new ReadOnlyObjectWrapper<>(new Perspective(
            "Project", new StackPane()));
    @Reference
    private ProjectService projectService;
    @Reference
    private SelectionService selectionService;
    @Reference
    private TabService tabService;
    @Inject
    private FXMLLoader fxmlLoader;

    @Activate
    private void activate() {
        GuiceContext context = new GuiceContext(this, () -> Arrays.asList(
                new ProjectServiceModule(projectService),
                new SelectionServiceModule(selectionService),
                new TabServiceModule(tabService)
        ));
        context.init();
        fxmlLoader.setClassLoader(Activator.class.getClassLoader());
        fxmlLoader.setLocation(FrameworkUtil.getBundle(Activator.class).getResource("/fxml/perspective.fxml"));
        try {
            Parent root = fxmlLoader.load();
            perspective.set(new Perspective("Project", root));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public ReadOnlyObjectProperty<Perspective> objectProperty() {
        return perspective.getReadOnlyProperty();
    }
}
