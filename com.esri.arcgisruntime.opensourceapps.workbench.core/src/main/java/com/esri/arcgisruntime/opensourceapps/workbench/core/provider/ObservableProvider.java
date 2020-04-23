package com.esri.arcgisruntime.opensourceapps.workbench.core.provider;

import javafx.beans.property.ReadOnlyObjectProperty;

public interface ObservableProvider<T> extends Provider<T> {
    ReadOnlyObjectProperty<T> objectProperty();

    @Override
    default T get() {
        return objectProperty().get();
    }
}
