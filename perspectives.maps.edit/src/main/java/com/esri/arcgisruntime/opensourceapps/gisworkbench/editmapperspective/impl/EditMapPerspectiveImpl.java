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

package com.esri.arcgisruntime.opensourceapps.gisworkbench.editmapperspective.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspective.service.PerspectiveProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspaces.service.Workspace;
import javafx.scene.Node;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;

@Component(immediate = true)
public class EditMapPerspectiveImpl implements PerspectiveProviderService {

    @Override
    public String getName() {
        return "Edit Map";
    }

    @Override
    public boolean isAvailableForWorkspace(Workspace workspace) {
        return workspace != null;
    }

    @Override
    public Node createNodeForWorkspace(Workspace workspace) {
        ProjectPerspective projectPerspective = new ProjectPerspective(workspace);
    }

    private boolean createFileIfNotExists(File file) {
        try {
            if (file.getParentFile().exists()) {
                Files.createFile(file.toPath());
                return true;
            }
        } catch (FileAlreadyExistsException ex) {
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private File getXmlFile(Workspace workspace, String xmlFileName) {
        return workspace.getMetadataDirectory().toPath().resolve(xmlFileName + ".xml").toFile();
    }
}
