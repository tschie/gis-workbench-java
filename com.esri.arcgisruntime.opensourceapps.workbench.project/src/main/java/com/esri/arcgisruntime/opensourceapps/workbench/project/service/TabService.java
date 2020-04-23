package com.esri.arcgisruntime.opensourceapps.workbench.project.service;

import javafx.beans.property.ReadOnlyListProperty;
import javafx.scene.control.Tab;

import java.util.List;

public interface TabService {
    void addTab(Tab tab);
    List<Tab> getTabs();
    ReadOnlyListProperty<Tab> tabsProperty();
}
