package com.esri.arcgisruntime.opensourceapps.gisworkbench.repository;

public interface MutableObservableMapRepository<K, T> extends MutableMapRepository<K, T>,
        ObservableMapRepository<K, T> {
}
