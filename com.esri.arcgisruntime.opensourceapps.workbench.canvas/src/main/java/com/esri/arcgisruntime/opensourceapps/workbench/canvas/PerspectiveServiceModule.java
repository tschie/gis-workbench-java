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

import com.esri.arcgisruntime.opensourceapps.workbench.perspective.service.PerspectiveService;
import com.google.inject.AbstractModule;

public class PerspectiveServiceModule extends AbstractModule {

    private final PerspectiveService perspectiveService;

    public PerspectiveServiceModule(PerspectiveService perspectiveService) {
        this.perspectiveService = perspectiveService;
    }

    @Override
    protected void configure() {
        bind(PerspectiveService.class).toInstance(perspectiveService);
    }
}