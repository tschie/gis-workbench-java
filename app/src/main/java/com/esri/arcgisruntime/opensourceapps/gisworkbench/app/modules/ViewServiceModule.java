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

package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.modules;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewService;
import com.google.inject.AbstractModule;

import java.util.Objects;

public class ViewServiceModule extends AbstractModule {

    private final ViewService viewService;

    /**
     * Create a module to inject the given viewService.
     *
     * @param viewService viewService to inject
     * @throws NullPointerException if viewService is null
     */
    public ViewServiceModule(ViewService viewService) {
        this.viewService = Objects.requireNonNull(viewService);
    }

    @Override
    protected void configure() {
        bind(ViewService.class).toInstance(viewService);
    }
}