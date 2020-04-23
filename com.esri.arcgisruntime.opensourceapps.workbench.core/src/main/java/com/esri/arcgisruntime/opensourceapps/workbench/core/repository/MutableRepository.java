package com.esri.arcgisruntime.opensourceapps.workbench.core.repository;

import java.util.List;

public interface MutableRepository<T> extends Repository<T> {
    void add(T object);

    void addAll(List<T> objects);

    void remove(T object);

    void removeAll();
}
