package com.esri.arcgisruntime.opensourceapps.workbench.core.repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public interface KeyedRepository<K, T> extends Repository<T> {
    T getByKey(K key);
    Set<Map.Entry<K, T>> getAllEntries();

    @Override
    default List<T> getAll() {
        return getAllEntries().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }
}
