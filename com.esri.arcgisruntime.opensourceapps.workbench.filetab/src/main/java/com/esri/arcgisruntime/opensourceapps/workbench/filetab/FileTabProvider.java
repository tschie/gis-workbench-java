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

package com.esri.arcgisruntime.opensourceapps.workbench.filetab;

import com.esri.arcgisruntime.opensourceapps.workbench.project.service.TabService;
import com.esri.arcgisruntime.opensourceapps.workbench.selection.service.SelectionService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.File;

@Component(immediate = true)
public class FileTabProvider {

    @Reference
    private SelectionService selectionService;

    @Reference
    private TabService tabService;

    @Activate
    private void activate() {
        selectionService.selectionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof File && ((File) newValue).isFile()) {
                tabService.addTab(new FileTab((File) newValue));
            }
        });
    }
}
