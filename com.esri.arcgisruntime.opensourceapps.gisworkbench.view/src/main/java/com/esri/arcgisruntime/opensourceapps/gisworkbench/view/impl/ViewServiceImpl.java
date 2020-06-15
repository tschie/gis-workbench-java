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

package com.esri.arcgisruntime.opensourceapps.gisworkbench.view.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewService;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ViewServiceImpl implements ViewService {

    private final ReadOnlyListWrapper<ViewProviderService> viewProviderServices = new ReadOnlyListWrapper<>(
            FXCollections.observableList(new CopyOnWriteArrayList<>())
    );

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addViewProvider(ViewProviderService viewProviderService) {
        viewProviderServices.add(viewProviderService);
    }

    public void removeViewProvider(ViewProviderService viewProviderService) {
        viewProviderServices.remove(viewProviderService);
    }

    @Override
    public ReadOnlyListProperty<ViewProviderService> observableListProperty() {
        return viewProviderServices.getReadOnlyProperty();
    }
}
