package com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service;

import java.util.concurrent.Flow;

public interface EventService {
    void emit(Event event);
    Flow.Publisher<Event> getPublisher();
}
