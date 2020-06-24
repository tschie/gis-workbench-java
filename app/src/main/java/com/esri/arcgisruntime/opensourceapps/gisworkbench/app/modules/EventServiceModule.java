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

import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.google.inject.AbstractModule;

import java.util.Objects;

public class EventServiceModule extends AbstractModule {

    private final EventService eventService;

    /**
     * Create a module to inject the given eventService.
     *
     * @param eventService eventService to inject
     * @throws NullPointerException if eventService is null
     */
    public EventServiceModule(EventService eventService) {
        this.eventService = Objects.requireNonNull(eventService);
    }

    @Override
    protected void configure() {
        bind(EventService.class).toInstance(eventService);
    }
}