package com.esri.arcgisruntime.opensourceapps.workbench.project.impl;

import com.esri.arcgisruntime.opensourceapps.workbench.perspective.service.PerspectiveService;
import com.esri.arcgisruntime.opensourceapps.workbench.preferences.service.PreferencesService;
import com.esri.arcgisruntime.opensourceapps.workbench.project.service.ProjectService;
import com.esri.arcgisruntime.opensourceapps.workbench.stage.service.StageService;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.File;
import java.util.prefs.Preferences;

@Component(immediate = true)
public class ProjectServiceImpl implements ProjectService {

    private final ReadOnlyObjectWrapper<File> project = new ReadOnlyObjectWrapper<>(null);
    @Reference
    private StageService stageService;
    @Reference
    private PreferencesService preferencesService;
    @Reference
    private PerspectiveService perspectiveService;

    @Activate
    private void activate() {
        if (!perspectiveService.getAll().isEmpty()) {
            loadProject();
        } else {
            perspectiveService.observableListProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.isEmpty()) {
                    loadProject();
                }
            });
        }
    }

    private void loadProject() {
        Preferences preferences = preferencesService.getPreferences();
        String projectPath = preferences.get("project", null);
        if (projectPath != null && new File(projectPath).isDirectory()) {
            setProject(new File(projectPath));
        }
    }

    @Override
    public void requestChangeProject() {
        Platform.runLater(() -> {
            Stage stage = stageService.getStage() != null ? stageService.getStage() : new Stage();
            DirectoryChooser directoryChooser = new DirectoryChooser();
            directoryChooser.setTitle("Choose project");
            File file = directoryChooser.showDialog(stage.getOwner());
            if (file != null) {
                setProject(file);
            }
        });
    }

    @Override
    public File getProject() {
        return projectProperty().get();
    }

    private void setProject(File file) {
        preferencesService.getPreferences().put("project", file.getAbsolutePath());
        project.set(file);
        perspectiveService.requestPerspective("Project");
        Stage stage = stageService.getStage();
        if (stage != null) {
            Platform.runLater(() -> stage.setTitle(file.getName() + " - " + file.getAbsolutePath()));
        } else {
            stageService.stageProperty().addListener(new ChangeListener<>() {
                @Override
                public void changed(ObservableValue<? extends Stage> observable, Stage oldValue, Stage newValue) {
                    newValue.setTitle(file.getName() + " - " + file.getAbsolutePath());
                    stageService.stageProperty().removeListener(this);
                }
            });
        }
    }

    @Override
    public ReadOnlyObjectProperty<File> projectProperty() {
        return project.getReadOnlyProperty();
    }
}
