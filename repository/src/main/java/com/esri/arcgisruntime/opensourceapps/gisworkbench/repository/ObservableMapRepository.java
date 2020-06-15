package com.esri.arcgisruntime.opensourceapps.gisworkbench.repository;

import javafx.beans.property.ReadOnlyMapProperty;

import java.util.Map;
import java.util.Set;

public interface ObservableMapRepository<K, T> extends MapRepository<K, T> {
    ReadOnlyMapProperty<K, T> observableMapProperty();

    @Override
    default Set<Map.Entry<K, T>> getAllEntries() {
        return observableMapProperty().get().entrySet();
    }
}
