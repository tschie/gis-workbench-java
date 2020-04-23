package com.esri.arcgisruntime.opensourceapps.workbench.menu.impl;

import com.esri.arcgisruntime.opensourceapps.workbench.menu.service.MenuProvider;
import com.esri.arcgisruntime.opensourceapps.workbench.menu.service.MenuService;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.concurrent.CopyOnWriteArrayList;

@Component(immediate = true)
public class MenuServiceImpl implements MenuService {

    private final ReadOnlyListWrapper<MenuProvider> menuProviders = new ReadOnlyListWrapper<>(
            FXCollections.observableList(new CopyOnWriteArrayList<>())
    );

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addMenuProvider(MenuProvider menuProvider) {
        menuProviders.add(menuProvider);
    }

    public void removeMenuProvider(MenuProvider menuProvider) {
        menuProviders.remove(menuProvider);
    }

    @Override
    public ReadOnlyListProperty<MenuProvider> observableListProperty() {
        return menuProviders.getReadOnlyProperty();
    }
}
