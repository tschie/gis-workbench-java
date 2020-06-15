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

package com.esri.arcgisruntime.opensourceapps.gisworkbench.perspective.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspective.service.PerspectiveProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspective.service.PerspectiveService;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.concurrent.CopyOnWriteArrayList;

@Component(immediate = true)
public class PerspectiveServiceImpl implements PerspectiveService {

    private final ReadOnlyListWrapper<PerspectiveProviderService> perspectiveProviderServices = new ReadOnlyListWrapper<>(
            FXCollections.observableList(new CopyOnWriteArrayList<>())
    );

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addPerspectiveProviderService(PerspectiveProviderService perspectiveProviderService) {
        perspectiveProviderServices.add(perspectiveProviderService);
    }

    public void removePerspectiveProviderService(PerspectiveProviderService perspectiveProviderService) {
        perspectiveProviderServices.remove(perspectiveProviderService);
    }

    @Override
    public ReadOnlyListProperty<PerspectiveProviderService> observableListProperty() {
        return perspectiveProviderServices.getReadOnlyProperty();
    }
}