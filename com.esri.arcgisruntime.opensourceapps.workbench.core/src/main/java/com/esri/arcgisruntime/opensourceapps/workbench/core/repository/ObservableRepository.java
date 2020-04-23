package com.esri.arcgisruntime.opensourceapps.workbench.core.repository;

import javafx.beans.property.ReadOnlyListProperty;

import java.util.List;

public interface ObservableRepository<T> extends Repository<T> {
    ReadOnlyListProperty<T> observableListProperty();

    @Override
    default List<T> getAll() {
        return observableListProperty().get();
    }
}
