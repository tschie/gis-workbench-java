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

package com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.maps.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.Perspective;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.PerspectiveProviderService;
import org.osgi.service.component.annotations.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component(immediate = true)
public class MapsPerspectiveProviderServiceImpl implements PerspectiveProviderService {

    @Override
    public Perspective getPerspective() {
        return new Perspective("Maps", new ArrayList<>(List.of("Portal Items", "Maps")), new ArrayList<>(),
                new ArrayList<>());
    }
}
