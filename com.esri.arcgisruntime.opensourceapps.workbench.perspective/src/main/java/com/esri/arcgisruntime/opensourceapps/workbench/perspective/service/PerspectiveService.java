package com.esri.arcgisruntime.opensourceapps.workbench.perspective.service;

import com.esri.arcgisruntime.opensourceapps.workbench.core.repository.ObservableRepository;
import javafx.beans.property.ObjectProperty;

public interface PerspectiveService extends ObservableRepository<PerspectiveProvider> {
    Perspective getCurrentPerspective();

    ObjectProperty<Perspective> currentPerspectiveProperty();

    void requestPerspective(String name);
}
