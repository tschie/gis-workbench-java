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

package com.esri.arcgisruntime.opensourceapps.gisworkbench.event.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.Event;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import io.reactivex.BackpressureStrategy;
import org.osgi.service.component.annotations.Component;
import org.reactivestreams.FlowAdapters;

import java.util.concurrent.Flow;

@Component(immediate = true)
public class EventServiceImpl implements EventService {

    private final RxEventBus rxEventBus = new RxEventBus();

    private final Flow.Publisher<Event> eventPublisher =
            FlowAdapters.toFlowPublisher(rxEventBus.toObservable().toFlowable(BackpressureStrategy.DROP));

    @Override
    public void emit(Event event) {
        rxEventBus.send(event);
    }

    @Override
    public Flow.Publisher<Event> getPublisher() {
        return eventPublisher;
    }
}