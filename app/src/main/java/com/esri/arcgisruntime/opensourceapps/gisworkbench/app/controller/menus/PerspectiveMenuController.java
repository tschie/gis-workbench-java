package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.controller.menus;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspective.service.PerspectiveProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspective.service.PerspectiveService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.view.Workbench;
import com.google.inject.Inject;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PerspectiveMenuController {

    @FXML
    private Menu menu;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    @Inject
    private Workbench workbench;

    @Inject
    private PerspectiveService perspectiveService;

    @FXML
    private void initialize() {
        menu.getItems().addAll(perspectiveService.getAll().stream()
            .map(this::createPerspectiveMenuItem)
            .collect(Collectors.toList()));

        perspectiveService.observableListProperty().addListener((ListChangeListener<PerspectiveProviderService>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    List<MenuItem> addedMenus = change.getAddedSubList().stream()
                            .map(this::createPerspectiveMenuItem)
                            .collect(Collectors.toList());
                    menu.getItems().addAll(addedMenus);
                } else if (change.wasRemoved()) {
                    menu.getItems().removeIf(m ->
                            change.getRemoved().contains(m.getUserData())
                    );
                }
            }
        });

        if (workbench.getPerspectiveProviderService() != null) {
            Optional<Toggle> selectedToggle = toggleGroup.getToggles().stream()
                    .filter(t -> t.getUserData().equals(workbench.getPerspectiveProviderService()))
                    .findFirst();
            selectedToggle.ifPresent(toggleGroup::selectToggle);
        }

        workbench.perspectiveProviderServiceProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Optional<Toggle> selectedToggle = toggleGroup.getToggles().stream()
                        .filter(t -> ((PerspectiveProviderService) t.getUserData()).getName().equals(newValue.getName()))
                        .findFirst();
                selectedToggle.ifPresent(toggleGroup::selectToggle);
            } else {
                toggleGroup.selectToggle(null);
            }
        });

        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                PerspectiveProviderService provider = (PerspectiveProviderService) newValue.getUserData();
                workbench.setPerspectiveProviderService(provider);
            } else {
                workbench.setPerspectiveProviderService(null);
            }
        });
    }

    private MenuItem createPerspectiveMenuItem(PerspectiveProviderService perspectiveProviderService) {
        RadioMenuItem menuItem = new RadioMenuItem(perspectiveProviderService.getName());
        menuItem.setUserData(perspectiveProviderService);
        menuItem.visibleProperty().bind(perspectiveProviderService.availableForWorkspaceProperty(workbench.getWorkspace()));
        menuItem.setToggleGroup(toggleGroup);
        return menuItem;
    }
}
