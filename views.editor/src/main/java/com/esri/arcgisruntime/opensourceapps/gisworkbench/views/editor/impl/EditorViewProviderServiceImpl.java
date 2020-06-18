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

package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service.EditorProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.view.EditorView;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.concurrent.CopyOnWriteArrayList;

@Component(immediate = true, property = {"name=Editor"})
public class EditorViewProviderServiceImpl implements ViewProviderService {

    private final ReadOnlyListWrapper<EditorProviderService> editorProviderServices = new ReadOnlyListWrapper<>(
            FXCollections.observableArrayList(new CopyOnWriteArrayList<>())
    );

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addEditorProviderService(EditorProviderService editorProviderService) {
        editorProviderServices.add(editorProviderService);
    }

    public void removeEditorProviderService(EditorProviderService editorProviderService) {
        editorProviderServices.remove(editorProviderService);
    }

    @Reference
    private EventService eventService;

    @Override
    public String getName() {
        return "Editor";
    }

    @Override
    public Node createNodeForWorkspace(Workspace workspace) {
        return new EditorView(workspace, editorProviderServices, eventService);
    }
}
