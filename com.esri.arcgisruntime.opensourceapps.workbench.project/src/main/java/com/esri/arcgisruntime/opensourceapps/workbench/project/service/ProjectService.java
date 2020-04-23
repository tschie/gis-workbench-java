package com.esri.arcgisruntime.opensourceapps.workbench.project.service;

import javafx.beans.property.ReadOnlyObjectProperty;

import java.io.File;

public interface ProjectService {
    File getProject();
    ReadOnlyObjectProperty<File> projectProperty();
    void requestChangeProject();
}
