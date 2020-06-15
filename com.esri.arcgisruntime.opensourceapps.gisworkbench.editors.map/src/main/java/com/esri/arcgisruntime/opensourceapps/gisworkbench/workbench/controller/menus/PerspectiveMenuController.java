package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.controller.menus;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PerspectiveMenuController {

    @FXML
    private Menu menu;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    /*@FXML
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

        if (perspectiveService.getCurrentPerspective() != null) {
            Optional<Toggle> selectedToggle = toggleGroup.getToggles().stream()
                    .filter(t -> t.getUserData().equals(perspectiveService.getCurrentPerspective()))
                    .findFirst();
            selectedToggle.ifPresent(toggleGroup::selectToggle);
        }

        perspectiveService.currentPerspectiveProperty().addListener((observable, oldValue, newValue) -> {
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
                perspectiveService.setCurrentPerspective(provider.create());
            } else {
                perspectiveService.setCurrentPerspective(null);
            }
        });
    }

    private MenuItem createPerspectiveMenuItem(PerspectiveProviderService perspectiveProviderService) {
        RadioMenuItem menuItem = new RadioMenuItem(perspectiveProviderService.getName());
        menuItem.setUserData(perspectiveProviderService);
        menuItem.setToggleGroup(toggleGroup);
        return menuItem;
    }*/
}
