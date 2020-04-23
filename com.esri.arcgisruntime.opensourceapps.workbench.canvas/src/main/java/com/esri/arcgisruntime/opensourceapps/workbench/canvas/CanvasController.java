package com.esri.arcgisruntime.opensourceapps.workbench.canvas;

import com.esri.arcgisruntime.opensourceapps.workbench.menu.service.MenuProvider;
import com.esri.arcgisruntime.opensourceapps.workbench.menu.service.MenuService;
import com.esri.arcgisruntime.opensourceapps.workbench.perspective.service.PerspectiveService;
import com.google.inject.Inject;
import javafx.beans.binding.Bindings;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;

import java.util.Objects;

public class CanvasController {

    @FXML
    private BorderPane borderPane;

    @FXML
    private MenuBar menuBar;

    @Inject
    private MenuService menuService;

    @Inject
    private PerspectiveService perspectiveService;

    @FXML
    void initialize() {
        menuService.getAll().stream()
                .map(MenuProvider::get)
                .filter(Objects::nonNull)
                .forEach(menu -> menuBar.getMenus().add(menu));

        menuService.observableListProperty().addListener((ListChangeListener<MenuProvider>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    change.getAddedSubList().forEach(menuProvider -> {
                        menuBar.getMenus().add(0, menuProvider.get());
                        menuProvider.objectProperty().addListener((observable, oldValue, newValue) -> {
                            if (newValue != null) {
                                menuBar.getMenus().add(0, newValue);
                            } else if (oldValue != null) {
                                menuBar.getMenus().remove(oldValue);
                            }
                        });
                    });
                }
            }
        });

        if (perspectiveService.getCurrentPerspective() != null) {
            borderPane.setCenter(perspectiveService.getCurrentPerspective().getRoot());
        }
        perspectiveService.currentPerspectiveProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                borderPane.setCenter(newValue.getRoot());
            } else {
                borderPane.setCenter(null);
            }
        });
    }
}
