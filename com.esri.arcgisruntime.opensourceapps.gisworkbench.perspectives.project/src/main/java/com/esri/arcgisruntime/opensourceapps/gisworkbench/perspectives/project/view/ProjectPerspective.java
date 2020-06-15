package com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.project.view;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.project.Activator;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.project.modules.ProjectPerspectiveModule;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.project.modules.ViewServiceModule;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import org.osgi.framework.FrameworkUtil;

import java.io.IOException;
import java.util.Arrays;

public class ProjectPerspective extends StackPane {

    @Inject
    private FXMLLoader fxmlLoader;

    private final Workspace workspace;

    public ProjectPerspective(Workspace workspace, ViewService viewService) {
        super();
        this.workspace = workspace;

        // injects an fxmlLoader containing the following modules
        GuiceContext guiceContext = new GuiceContext(this, () -> Arrays.asList(
                new ProjectPerspectiveModule(this),
                new ViewServiceModule(viewService)
        ));
        guiceContext.init();

        initialize();
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    private void initialize() {
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        fxmlLoader.setClassLoader(Activator.class.getClassLoader());
        fxmlLoader.setLocation(FrameworkUtil.getBundle(Activator.class).getResource("/fxml/projectPerspective.fxml"));
        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
