package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.view;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.Activator;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.model.Context;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.modules.ContextModule;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.modules.PortalItemComponentServiceModule;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.modules.WorkspacesServiceModule;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.service.PortalItemComponentService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.util.XmlFileSerializer;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspaces.service.WorkspacesService;
import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import javafx.beans.Observable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.osgi.framework.FrameworkUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Workbench extends Stage {

    @Inject
    private FXMLLoader fxmlLoader;

    private final XmlFileSerializer xmlFileSerializer = new XmlFileSerializer();

    private final Context context;

    public Workbench(File workspace, WorkspacesService workspacesService, PortalItemComponentService portalItemComponentService) {
        super();
        context = new Context(workspace, this);

        GuiceContext guiceContext = new GuiceContext(this, () -> Arrays.asList(
            new ContextModule(context),
            new WorkspacesServiceModule(workspacesService),
            new PortalItemComponentServiceModule(portalItemComponentService)
        ));
        guiceContext.init();

        fxmlLoader.setClassLoader(Activator.class.getClassLoader());
        fxmlLoader.setLocation(FrameworkUtil.getBundle(Activator.class).getResource("/fxml/workbench.fxml"));
        try {
            Parent root = fxmlLoader.load();
            setScene(new Scene(root));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        List<Image> icons = Stream.of(
                "/assets/arcgis-open-source-apps-32.png",
                "/assets/arcgis-open-source-apps-64.png",
                "/assets/arcgis-open-source-apps-128.png",
                "/assets/arcgis-open-source-apps-256.png",
                "/assets/arcgis-open-source-apps-512.png"
        ).map(path -> {
            try {
                return new Image(FrameworkUtil.getBundle(Activator.class).getResource(path).openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        getIcons().addAll(icons);

        initializeState();

        xProperty().addListener(this::serializeState);
        yProperty().addListener(this::serializeState);
        widthProperty().addListener(this::serializeState);
        heightProperty().addListener(this::serializeState);
        maximizedProperty().addListener(this::serializeState);
    }

    public Context getContext() {
        return context;
    }

    private void initializeState() {
        State deserializedState = deserializeState();
        if (deserializedState != null) {
            applyState(deserializedState);
        } else {
            setWidth(600);
            setHeight(400);
        }
    }

    private File getWindowXmlFile() {
        if (context.getWorkspace() == null) {
            return null;
        }
        return context.getWorkspace().toPath().resolve(".workbench").resolve("window.xml").toFile();
    }

    private State deserializeState() {
        File windowXmlFile = getWindowXmlFile();
        if (windowXmlFile != null && windowXmlFile.exists()) {
            return xmlFileSerializer.deserialize(windowXmlFile, State.class);
        }
        return null;
    }

    private void applyState(State state) {
        setX(state.getX());
        setY(state.getY());
        setWidth(state.getWidth());
        setHeight(state.getHeight());
        setMaximized(state.getMaximized());
    }

    private void serializeState(Observable observable) {
        serializeState();
    }

    private void serializeState() {
        File windowXmlFile = getWindowXmlFile();
        // create file if it does not exist
        if (windowXmlFile != null && !windowXmlFile.exists()) {
            try {
                if (!windowXmlFile.getParentFile().exists()) {
                    Files.createDirectory(windowXmlFile.getParentFile().toPath());
                }
                Files.createFile(windowXmlFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // serialize state to file
        if (windowXmlFile != null && windowXmlFile.exists()) {
            State state = new State(getX(), getY(), getWidth(), getHeight(), isMaximized());
            xmlFileSerializer.serialize(getWindowXmlFile(), state);
        }
    }
}
