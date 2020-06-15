package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.service.PortalItemComponentService;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.beans.property.MapProperty;
import javafx.beans.property.SimpleMapProperty;
import javafx.collections.FXCollections;
import org.osgi.service.component.annotations.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component(immediate = true)
public class PortalItemComponentServiceImpl implements PortalItemComponentService {

    private final MapProperty<UUID, PortalItem> values = new SimpleMapProperty<>(FXCollections.observableHashMap());

    @Override
    public void put(UUID id, PortalItem url) {
        values.put(id, url);
    }

    @Override
    public void putAll(Map<UUID, PortalItem> map) {
        values.putAll(map);
    }

    @Override
    public void remove(UUID uuid) {
        values.remove(uuid);
    }

    @Override
    public void removeAll(List<UUID> list) {
        list.forEach(values::remove);
    }

    @Override
    public PortalItem getByKey(UUID uuid) {
        return values.get(uuid);
    }

    @Override
    public MapProperty<UUID, PortalItem> observableMapProperty() {
        return values;
    }
}