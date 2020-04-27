/*
 * Copyright 2020 Esri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

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
