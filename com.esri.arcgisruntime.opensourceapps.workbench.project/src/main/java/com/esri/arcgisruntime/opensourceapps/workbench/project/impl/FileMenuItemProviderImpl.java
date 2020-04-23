package com.esri.arcgisruntime.opensourceapps.workbench.project.impl;

import com.esri.arcgisruntime.opensourceapps.workbench.filemenu.service.FileMenuItemProvider;
import com.esri.arcgisruntime.opensourceapps.workbench.project.service.ProjectService;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import javafx.scene.control.MenuItem;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true)
public class FileMenuItemProviderImpl implements FileMenuItemProvider {

    private final MenuItem openProjectMenuItem;
    private final ReadOnlyListWrapper<MenuItem> menuItems;

    @Reference
    private ProjectService projectService;

    public FileMenuItemProviderImpl() {
        openProjectMenuItem = new MenuItem("Open...");
        menuItems = new ReadOnlyListWrapper<>(FXCollections.observableArrayList(openProjectMenuItem));
    }

    @Activate
    private void activate() {
        openProjectMenuItem.setOnAction(e -> projectService.requestChangeProject());
    }

    @Override
    public ReadOnlyListProperty<MenuItem> observableListProperty() {
        return menuItems.getReadOnlyProperty();
    }
}
