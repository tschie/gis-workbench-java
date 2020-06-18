package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.controller.menus;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.Perspective;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.PerspectiveProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.PerspectiveService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.view.Workbench;
import com.google.inject.Inject;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.stream.Collectors;

public class PerspectiveMenuController {

    @FXML
    private Menu menu;

    @FXML
    private SeparatorMenuItem separatorMenuItem;

    private final ToggleGroup toggleGroup = new ToggleGroup();

    @Inject
    private Workbench workbench;

    @Inject
    private PerspectiveService perspectiveService;

    @FXML
    private void initialize() {
        if (workbench.getWorkspace() != null) {
            // add perspective presets from providers
            menu.getItems().addAll(menu.getItems().indexOf(separatorMenuItem), perspectiveService.getAll().stream()
                    .map(PerspectiveProviderService::getPerspective)
                    .map(this::createPerspectiveMenuItem)
                    .collect(Collectors.toList()));

            perspectiveService.observableListProperty().addListener((ListChangeListener<PerspectiveProviderService>) change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        List<MenuItem> addedMenus = change.getAddedSubList().stream()
                                .map(PerspectiveProviderService::getPerspective)
                                .map(this::createPerspectiveMenuItem)
                                .collect(Collectors.toList());
                        menu.getItems().addAll(menu.getItems().indexOf(separatorMenuItem), addedMenus);
                    } else if (change.wasRemoved()) {
                        menu.getItems().removeIf(m ->
                                change.getRemoved().stream()
                                        .map(PerspectiveProviderService::getPerspective)
                                        .map(Perspective::getName)
                                        .collect(Collectors.toList())
                                        .contains(((Perspective) m.getUserData()).getName())
                        );
                    }
                }
            });

            // add custom perspectives
            menu.getItems().addAll(menu.getItems().indexOf(separatorMenuItem) + 1,
                    workbench.getCustomPerspectives().stream()
                    .map(this::createPerspectiveMenuItem)
                    .collect(Collectors.toList()));

            workbench.getCustomPerspectives().addListener((ListChangeListener<Perspective>) change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        List<MenuItem> addedMenus = change.getAddedSubList().stream()
                                .map(this::createPerspectiveMenuItem)
                                .collect(Collectors.toList());
                        menu.getItems().addAll(menu.getItems().indexOf(separatorMenuItem) + 1, addedMenus);
                    } else if (change.wasRemoved()) {
                        menu.getItems().removeIf(m -> change.getRemoved().stream()
                                .map(Perspective::getName)
                                .collect(Collectors.toList())
                                .contains(((Perspective) m.getUserData()).getName())
                        );
                    }
                }
            });

            // handle perspective selection
            if (workbench.getPerspective() != null) {
                toggleGroup.getToggles().stream()
                        .filter(t -> ((Perspective) t.getUserData()).getName().equals(workbench.getPerspective().getName()))
                        .findFirst()
                        .ifPresent(toggleGroup::selectToggle);
            }

            workbench.perspectiveProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    toggleGroup.getToggles().stream()
                            .filter(t -> ((Perspective) t.getUserData()).getName().equals(newValue.getName()))
                            .findFirst()
                            .ifPresent(toggleGroup::selectToggle);
                } else {
                    toggleGroup.selectToggle(null);
                }
            });

            toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    workbench.setPerspective((Perspective) newValue.getUserData());
                } else {
                    workbench.setPerspective(null);
                }
            });
        }
    }

    private MenuItem createPerspectiveMenuItem(Perspective perspective) {
        RadioMenuItem menuItem = new RadioMenuItem(perspective.getName());
        menuItem.setUserData(perspective);
        menuItem.setToggleGroup(toggleGroup);
        return menuItem;
    }
}
