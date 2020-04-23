package com.esri.arcgisruntime.opensourceapps.workbench.core.repository;

import java.util.List;
import java.util.Map;

public interface MutableKeyedRepository<K, T> extends KeyedRepository<K, T> {
    void put(K key, T object);
    void putAll(Map<K, T> objectsByKey);
    void remove(K key);
    void removeAll(List<K> keys);
}
