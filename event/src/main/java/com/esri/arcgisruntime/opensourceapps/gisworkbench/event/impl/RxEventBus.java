package com.esri.arcgisruntime.opensourceapps.gisworkbench.event.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.Event;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxEventBus {

    private final PublishSubject<Event<?>> bus = PublishSubject.create();

    public void send(Event<?> event) {
        bus.onNext(event);
    }

    public Observable<Event<?>> toObservable() {
        return bus;
    }

}
