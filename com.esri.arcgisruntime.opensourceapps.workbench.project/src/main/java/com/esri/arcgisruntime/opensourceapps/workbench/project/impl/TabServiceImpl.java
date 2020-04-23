package com.esri.arcgisruntime.opensourceapps.workbench.project.impl;

import com.esri.arcgisruntime.opensourceapps.workbench.project.service.TabService;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.Tab;
import org.osgi.service.component.annotations.Component;

import java.util.List;

@Component(immediate = true)
public class TabServiceImpl implements TabService {

    private final ReadOnlyListWrapper<Tab> tabs = new ReadOnlyListWrapper<>(FXCollections.observableArrayList());

    @Override
    public void addTab(Tab tab) {
        tabs.add(tab);
    }

    @Override
    public List<Tab> getTabs() {
        return tabsProperty().get();
    }

    @Override
    public ReadOnlyListProperty<Tab> tabsProperty() {
        return tabs.getReadOnlyProperty();
    }
}
