package com.esri.arcgisruntime.opensourceapps.workbench.stage.impl;

import com.esri.arcgisruntime.opensourceapps.workbench.stage.Activator;
import com.esri.arcgisruntime.opensourceapps.workbench.stage.service.StageService;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true)
public class StageServiceImpl implements StageService {

    private final ReadOnlyObjectWrapper<Stage> stage = new ReadOnlyObjectWrapper<>(null);

    @Activate
    private void activate() {
        try {
            Platform.startup(() -> {
                Stage primaryStage = new Stage();
                primaryStage.setScene(new Scene(new StackPane()));
                primaryStage.setOnCloseRequest(windowEvent -> {
                    Alert closeConfirmationDialog = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you" +
                            " want to exit?");
                    closeConfirmationDialog.setTitle("Exit");
                    closeConfirmationDialog.setHeaderText("");
                    closeConfirmationDialog.showAndWait().ifPresent(buttonType -> {
                        if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                            try {
                                Bundle thisBundle = FrameworkUtil.getBundle(Activator.class);
                                if (thisBundle != null) {
                                    thisBundle.getBundleContext().getBundle(0).stop();
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            windowEvent.consume();
                        }
                    });
                });
                primaryStage.show();
                stage.set(primaryStage);
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Stage getStage() {
        return stageProperty().get();
    }

    @Override
    public ReadOnlyObjectProperty<Stage> stageProperty() {
        return stage.getReadOnlyProperty();
    }
}
