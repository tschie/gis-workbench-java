package com.esri.arcgisruntime.opensourceapps.gisworkbench.app.controller;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.model.Editors;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.app.view.Workbench;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service.Editor;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service.EditorProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service.EditorService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.events.editor.OpenEditorEvent;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.events.editor.SelectEditorEvent;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.view.service.ViewService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.xml.XmlFileSerializer;
import com.google.inject.Inject;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import org.reactivestreams.FlowAdapters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;
import java.util.stream.Collectors;

public class EditorController {

    @FXML
    private StackPane stackPane;

    @FXML
    private TabPane tabPane;

    @Inject
    private Workbench workbench;

    @Inject
    private ViewService viewService;

    @Inject
    private EditorService editorService;

    @Inject
    private EventService eventService;

    private final XmlFileSerializer xmlFileSerializer = new XmlFileSerializer();
    private Disposable eventSubscription;
    private Workspace workspace;

    @FXML
    private void initialize() {
        workspace = workbench.getWorkspace();
        if (workspace != null) {

            // subscribe to events
            this.eventSubscription = Flowable.fromPublisher(FlowAdapters.toPublisher(eventService.getPublisher()))
                .subscribe(event -> {
                    try {
                        // track events for this workspace
                        if (event.getWorkspace().getRootDirectory().equals(workspace.getRootDirectory())) {
                            if (event instanceof SelectEditorEvent) {
                                // select tab for selected ID
                                UUID id = ((SelectEditorEvent) event).getPayload();
                                tabPane.getTabs().stream()
                                    .filter(tab -> tab.getUserData().equals(id))
                                    .findFirst()
                                    .ifPresent(tab -> tabPane.getSelectionModel().select(tab));
                            } else if (event instanceof OpenEditorEvent) {
                                // if already open, select tab; else, create new editor tab if it has supported editors
                                UUID id = ((OpenEditorEvent) event).getPayload();
                                tabPane.getTabs().stream()
                                    .filter(tab -> tab.getUserData().equals(id))
                                    .findFirst()
                                    .ifPresentOrElse(tab -> tabPane.getSelectionModel().select(tab), () -> {
                                        if (editorService.getAll().stream().anyMatch(editorProviderService -> editorProviderService.supports(workspace, id))) {
                                            Tab tab = createTab(id);
                                            tabPane.getTabs().add(tab);
                                            tabPane.getSelectionModel().select(tab);
                                        }
                                    });
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

            // deserialize tabs
            Editors editors = deserialize();
            if (editors != null) {
                // still create tabs if it has no supported editors so user can choose to close it
                tabPane.getTabs().addAll(editors.getIds().stream()
                    .map(this::createTab)
                    .collect(Collectors.toList())
                );
            }

            // serialize tabs
            tabPane.getTabs().addListener((ListChangeListener<Tab>) change -> {
                while (change.next()) {
                    serialize();
                }
            });
        }
    }

    /**
     * Creates a tab for the given entity ID whose content is a tab pane containing tabs for all editors supporting the
     * entity. The tab will be empty if it has no supporting editors, allowing the user to close it. The ID is saved to
     * the tab's user data.
     *
     * @param id entity id
     * @return tab
     */
    private Tab createTab(UUID id) {

        Tab tab = new Tab("?");
        tab.setUserData(id);
        TabPane editorsTabPane = new TabPane();
        editorsTabPane.setSide(Side.BOTTOM);
        editorsTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tab.setContent(editorsTabPane);

        // add tabs for all supporting editors
        editorsTabPane.getTabs().addAll(editorService.getAll().stream()
            .filter(editorProviderService -> editorProviderService.supports(workspace, id))
            .map(editorProviderService -> editorProviderService.create(workspace, id))
            .map(this::createEditorTab)
            .collect(Collectors.toList()));

        // if support changes for an editor, add or remove it
        editorService.getAll().forEach(editorProviderService -> {
            editorProviderService.supportsProperty(workspace, id).addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    editorsTabPane.getTabs().add(createEditorTab(editorProviderService.create(workspace, id)));
                } else {
                    editorsTabPane.getTabs().removeIf(t -> ((Editor) t.getUserData()).getName().equals(editorProviderService.getName()));
                }
            });
        });

        // observe changes to editor providers
        editorService.observableListProperty().addListener((ListChangeListener<EditorProviderService>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    editorsTabPane.getTabs().addAll(change.getAddedSubList().stream()
                        .filter(editorProviderService -> editorProviderService.supports(workspace, id))
                        .map(editorProviderService -> editorProviderService.create(workspace, id))
                        .map(this::createEditorTab)
                        .collect(Collectors.toList()));

                    // watch supported property
                    editorService.getAll().forEach(editorProviderService -> {
                        editorProviderService.supportsProperty(workspace, id).addListener((observable, oldValue, newValue) -> {
                            if (newValue) {
                                editorsTabPane.getTabs().add(createEditorTab(editorProviderService.create(workspace, id)));
                            } else {
                                editorsTabPane.getTabs().removeIf(t -> ((Editor) t.getUserData()).getName().equals(editorProviderService.getName()));
                            }
                        });
                    });
                } else if (change.wasRemoved()) {
                    editorsTabPane.getTabs().removeIf(t -> change.getRemoved().stream()
                        .map(EditorProviderService::getName)
                        .collect(Collectors.toList())
                        .contains(((Editor) t.getUserData()).getName())
                    );
                }
            }
        });

        // select the first editor tab if one exists
        if (!editorsTabPane.getTabs().isEmpty()) {
            tab.setText(((Editor) editorsTabPane.getTabs().get(0).getUserData()).getDisplayText());
        }

        editorsTabPane.getTabs().addListener((ListChangeListener<Tab>) change -> {
            while (change.next()) {
                if (editorsTabPane.getTabs().isEmpty()) {
                    tab.textProperty().unbind();
                    tab.setText("?");
                }
            }
        });

        // bind text to selected editor tab's display text
        // TODO: serialize selected editor for tab
        editorsTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                tab.textProperty().unbind();
                tab.setText("?");
            } else {
                tab.textProperty().unbind();
                tab.textProperty().bind(((Editor) newValue.getUserData()).displayTextProperty());
            }
        });

        return tab;
    }

    /**
     * Creates a tab for a specific editor. The editor is saved to the tab's user data.
     *
     * @param editor editor
     * @return editor tab
     */
    private Tab createEditorTab(Editor editor) {
        Tab tab = new Tab(editor.getName());
        tab.setContent(editor.getNode());
        tab.setUserData(editor);
        return tab;
    }

    private File getXmlFile() {
        return workspace.getMetadataDirectory().toPath().resolve("editor.xml").toFile();
    }

    private Editors deserialize() {
        File xmlFile = getXmlFile();
        if (xmlFile.exists()) {
            return xmlFileSerializer.deserialize(xmlFile, Editors.class);
        }
        return null;
    }

    private void serialize() {
        File xmlFile = getXmlFile();
        // create file if it does not exist
        if (!xmlFile.exists()) {
            try {
                if (!xmlFile.getParentFile().exists()) {
                    Files.createDirectory(xmlFile.getParentFile().toPath());
                }
                Files.createFile(xmlFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // serialize state to file
        if (xmlFile.exists()) {
            Editors editors = new Editors(tabPane.getTabs().stream()
                .map(tab -> (UUID) tab.getUserData())
                .collect(Collectors.toList()));
            xmlFileSerializer.serialize(getXmlFile(), editors);
        }
    }
}
