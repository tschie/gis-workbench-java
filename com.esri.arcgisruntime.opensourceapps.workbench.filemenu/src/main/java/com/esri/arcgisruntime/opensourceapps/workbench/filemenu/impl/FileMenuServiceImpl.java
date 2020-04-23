package com.esri.arcgisruntime.opensourceapps.workbench.filemenu.impl;

import com.esri.arcgisruntime.opensourceapps.workbench.filemenu.service.FileMenuItemProvider;
import com.esri.arcgisruntime.opensourceapps.workbench.filemenu.service.FileMenuService;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.concurrent.CopyOnWriteArrayList;

@Component(immediate = true)
public class FileMenuServiceImpl implements FileMenuService {

    private final ReadOnlyListWrapper<FileMenuItemProvider> fileMenuItemProviders = new ReadOnlyListWrapper<>(
            FXCollections.observableList(new CopyOnWriteArrayList<>())
    );

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addFileMenuItemProvider(FileMenuItemProvider fileMenuItemProvider) {
        fileMenuItemProviders.add(fileMenuItemProvider);
    }

    public void removeFileMenuItemProvider(FileMenuItemProvider fileMenuItemProvider) {
        fileMenuItemProviders.remove(fileMenuItemProvider);
    }
    
    @Override
    public ReadOnlyListProperty<FileMenuItemProvider> observableListProperty() {
        return fileMenuItemProviders.getReadOnlyProperty();
    }
}
