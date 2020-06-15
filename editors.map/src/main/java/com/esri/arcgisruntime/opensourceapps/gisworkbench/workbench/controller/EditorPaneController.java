package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.controller;

import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.model.Context;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.service.PortalItemComponentService;
import com.esri.arcgisruntime.portal.PortalItem;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.File;
import java.util.UUID;

public class EditorPaneController {

    @Inject
    private Context context;

    @Inject
    private PortalItemComponentService portalItemComponentService;

    @FXML
    private TabPane tabPane;

    @FXML
    private void initialize() {
        context.selectionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                tabPane.getSelectionModel().select(null);
            } else {
                tabPane.getTabs().stream()
                        .filter(t -> t.getUserData().equals(newValue))
                        .findFirst()
                        .ifPresentOrElse(tab -> tabPane.getSelectionModel().select(tab), () -> {
                    if (newValue instanceof UUID) {
                        PortalItem portalItem = portalItemComponentService.getByKey((UUID) newValue);
                        portalItem.loadAsync();
                        ArcGISMap map = new ArcGISMap(portalItem);
                        Tab mapViewTab = new MapViewTab(map);
                        mapViewTab.setUserData(newValue);
                        tabPane.getTabs().add(mapViewTab);
                        tabPane.getSelectionModel().select(mapViewTab);
                    } else if (newValue instanceof File && ((File) newValue).isFile()) {
                        Tab textFileTab = new FileTab((File) newValue);
                        textFileTab.setUserData(newValue);
                        tabPane.getTabs().add(textFileTab);
                        tabPane.getSelectionModel().select(textFileTab);
                    } else {
                        System.out.println(newValue);
                    }
                });
            }
        });
    }
}
