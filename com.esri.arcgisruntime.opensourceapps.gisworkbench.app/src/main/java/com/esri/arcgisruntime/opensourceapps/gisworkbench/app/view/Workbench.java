package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.view;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.Activator;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.modules.PerspectiveServiceModule;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.modules.WorkbenchModule;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.modules.WorkspaceServiceModule;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.service.WorkspaceService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspective.service.PerspectiveProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.perspective.service.PerspectiveService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.xml.XmlFileSerializer;
import com.gluonhq.ignite.guice.GuiceContext;
import com.google.inject.Inject;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.osgi.framework.FrameworkUtil;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
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

    private final Workspace workspace;

    private final ReadOnlyObjectWrapper<PerspectiveProviderService> perspectiveProviderServiceWrapper = new ReadOnlyObjectWrapper<>(null);

    private final PerspectiveService perspectiveService;

    public Workbench(Workspace workspace, WorkspaceService workspaceService, PerspectiveService perspectiveService) {
        super();
        this.workspace = workspace;
        this.perspectiveService = perspectiveService;

        // injects an fxmlLoader containing the following modules
        GuiceContext guiceContext = new GuiceContext(this, () -> Arrays.asList(
                new WorkbenchModule(this),
                new WorkspaceServiceModule(workspaceService),
                new PerspectiveServiceModule(perspectiveService)
        ));
        guiceContext.init();

        initialize();
    }

    private void initialize() {
        // initialize scene from FXML
        fxmlLoader.setClassLoader(Activator.class.getClassLoader());
        fxmlLoader.setLocation(FrameworkUtil.getBundle(Activator.class).getResource("/fxml/workbench.fxml"));
        try {
            Parent root = fxmlLoader.load();
            setScene(new Scene(root));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // add stage icon(s)
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

        // set up stage for deserialization/serialization
        if (workspace != null && workspace.getRootDirectory().exists()) {
            State deserializedState = deserializeState();
            if (deserializedState != null) {
                applyState(deserializedState);
            } else {
                setWidth(600);
                setHeight(400);
            }
            xProperty().addListener(this::serializeState);
            yProperty().addListener(this::serializeState);
            widthProperty().addListener(this::serializeState);
            heightProperty().addListener(this::serializeState);
            maximizedProperty().addListener(this::serializeState);
        } else {
            setWidth(600);
            setHeight(400);
        }
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    public PerspectiveProviderService getPerspectiveProviderService() {
        return perspectiveProviderServiceProperty().get();
    }

    public ReadOnlyObjectProperty<PerspectiveProviderService> perspectiveProviderServiceProperty() {
        return perspectiveProviderServiceWrapper.getReadOnlyProperty();
    }

    public void setPerspectiveProviderService(PerspectiveProviderService perspectiveProviderService) {
        perspectiveProviderServiceWrapper.set(perspectiveProviderService);
    }

    private File getWorkbenchXmlFile() {
        assert workspace != null;
        return getWorkspace().getMetadataDirectory().toPath().resolve(getClass().getPackageName() + ".xml").toFile();
    }

    private State deserializeState() {
        File windowXmlFile = getWorkbenchXmlFile();
        if (windowXmlFile.exists()) {
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
        if (getPerspectiveProviderService() == null || !getPerspectiveProviderService().getName().equals(state.getPerspective())) {
            perspectiveService.getAll().stream()
                    .filter(p -> p.getName().equals(state.getPerspective()))
                    .findFirst()
                    .ifPresent(perspectiveProviderServiceWrapper::set);
        }
    }

    private void serializeState(Observable observable) {
        serializeState();
    }

    private void serializeState() {
        File windowXmlFile = getWorkbenchXmlFile();
        // create file if it does not exist
        if (!windowXmlFile.exists()) {
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
        if (windowXmlFile.exists()) {
            State state = new State(getX(), getY(), getWidth(), getHeight(), isMaximized(),
                    getPerspectiveProviderService() != null ? getPerspectiveProviderService().getName() : null);
            xmlFileSerializer.serialize(getWorkbenchXmlFile(), state);
        }
    }

    @XmlRootElement(name = "Workbench")
    public static class State {

        @XmlAttribute
        private final Double x;

        @XmlAttribute
        private final Double y;

        @XmlAttribute
        private final Double width;

        @XmlAttribute
        private final Double height;

        @XmlAttribute
        private final Boolean maximized;

        @XmlAttribute
        private final String perspective;

        public State() {
            this(0.0, 0.0, 0.0, 0.0, false, null);
        }

        public State(Double x, Double y, Double width, Double height, Boolean maximized, String perspective) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.maximized = maximized;
            this.perspective = perspective;
        }

        public Double getX() {
            return x;
        }

        public Double getY() {
            return y;
        }

        public Double getWidth() {
            return width;
        }

        public Double getHeight() {
            return height;
        }

        public Boolean getMaximized() {
            return maximized;
        }

        public String getPerspective() {
            return perspective;
        }
    }
}
