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

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.view.Workbench;
import com.google.inject.AbstractModule;

import java.util.Objects;

/**
 * Modules containing a reference to a Workbench. May be used by a Workbench to inject a reference to itself into a
 * child element.
 */
public class WorkbenchModule extends AbstractModule {

    private final Workbench workbench;

    /**
     * Create a module to inject the given workbench.
     *
     * @param workbench workbench to inject
     * @throws NullPointerException if workbench is null
     */
    public WorkbenchModule(Workbench workbench) {
        this.workbench = Objects.requireNonNull(workbench);
    }

    @Override
    protected void configure() {
        bind(Workbench.class).toInstance(workbench);
    }
}