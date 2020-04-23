package com.esri.arcgisruntime.opensourceapps.workbench.canvas;

import com.esri.arcgisruntime.opensourceapps.workbench.menu.service.MenuService;
import com.esri.arcgisruntime.opensourceapps.workbench.perspective.service.PerspectiveService;
import com.esri.arcgisruntime.opensourceapps.workbench.stage.service.StageService;
import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.util.Arrays;

@Component(immediate = true)
public class Canvas {

    @Reference
    private StageService stageService;

    @Reference
    private PerspectiveService perspectiveService;

    @Reference
    private MenuService menuService;

    @Inject
    private FXMLLoader fxmlLoader;

    @Activate
    private void activate() {
        if (stageService.getStage() != null) {
            setScene(stageService.getStage());
        } else {
            stageService.stageProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    setScene(newValue);
                }
            });
        }
    }

    private void setScene(Stage stage) {
        GuiceContext context = new GuiceContext(this, () -> Arrays.asList(
                new MenuServiceModule(menuService),
                new PerspectiveServiceModule(perspectiveService)
        ));
        context.init();
        fxmlLoader.setClassLoader(Activator.class.getClassLoader());
        fxmlLoader.setLocation(FrameworkUtil.getBundle(Activator.class).getResource("/fxml/main.fxml"));
        try {
            Parent root = fxmlLoader.load();
            stage.getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
