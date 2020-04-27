package com.esri.arcgisruntime.opensourceapps.workbench.perspective.impl;

import com.esri.arcgisruntime.opensourceapps.workbench.perspective.service.Perspective;
import com.esri.arcgisruntime.opensourceapps.workbench.perspective.service.PerspectiveProvider;
import com.esri.arcgisruntime.opensourceapps.workbench.perspective.service.PerspectiveService;
import com.esri.arcgisruntime.opensourceapps.workbench.stage.service.StageService;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component(immediate = true)
public class PerspectiveServiceImpl implements PerspectiveService {

    private final ReadOnlyListWrapper<PerspectiveProvider> perspectiveProviders = new ReadOnlyListWrapper<>(
            FXCollections.observableList(new CopyOnWriteArrayList<>())
    );
    private final ObjectProperty<Perspective> currentPerspective = new SimpleObjectProperty<>(null);
    @Reference
    private StageService stageService;

    @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    public void addPerspectiveProvider(PerspectiveProvider perspectiveProvider) {
        perspectiveProviders.add(perspectiveProvider);
    }

    public void removePerspectiveProvider(PerspectiveProvider perspectiveProvider) {
        perspectiveProviders.remove(perspectiveProvider);
    }

    @Override
    public ReadOnlyListProperty<PerspectiveProvider> observableListProperty() {
        return perspectiveProviders.getReadOnlyProperty();
    }

    @Override
    public Perspective getCurrentPerspective() {
        return currentPerspectiveProperty().get();
    }

    private void setCurrentPerspective(Perspective perspective) {
        Platform.runLater(() -> currentPerspective.set(perspective));
    }

    @Override
    public ObjectProperty<Perspective> currentPerspectiveProperty() {
        return currentPerspective;
    }

    @Override
    public void requestPerspective(String name) {
        Perspective perspective = perspectiveProviders.stream()
                .map(PerspectiveProvider::get)
                .filter(p -> name.equals(p.getName()))
                .findFirst()
                .orElse(null);
        if (perspective != null) {
            if (getCurrentPerspective() == null) {
                setCurrentPerspective(perspective);
            } else if (perspective != getCurrentPerspective()) {
                Optional<ButtonType> choice = new Alert(Alert.AlertType.CONFIRMATION,
                        "Set perspective " + perspective.getName() + "?").showAndWait();
                if (choice.isPresent() && choice.get() == ButtonType.OK) {
                    setCurrentPerspective(perspective);
                }
            }
        } else {
            List<Perspective> perspectives = perspectiveProviders.get().stream()
                    .map(PerspectiveProvider::get)
                    .collect(Collectors.toList());
            if (!perspectives.isEmpty()) {
                Platform.runLater(() -> {
                    new ChoiceDialog<>(perspectives.get(0), perspectives)
                            .showAndWait()
                            .ifPresent(this::setCurrentPerspective);
                });
            }
        }
    }
}
