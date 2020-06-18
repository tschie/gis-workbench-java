package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.view;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service.Editor;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service.EditorProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.reactivestreams.FlowAdapters;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EditorView extends TabPane {

    private final List<String> eventTypes = List.of("select", "open");
    private final Workspace workspace;
    private final ObservableList<EditorProviderService> editorProviderServices;
    private final Disposable eventSubscription;

    public EditorView(Workspace workspace,
                      ObservableList<EditorProviderService> editorProviderServices,
                      EventService eventService) {
        this.workspace = workspace;
        this.editorProviderServices = editorProviderServices;

        setTabClosingPolicy(TabClosingPolicy.ALL_TABS);

        // subscribe to events
        this.eventSubscription = Flowable.fromPublisher(FlowAdapters.toPublisher(eventService.getPublisher()))
                .subscribe(event -> {
                    try {
                        if (event.getWorkspace().getRootDirectory().equals(workspace.getRootDirectory()) && !Collections.disjoint(event.getEventTypes(), eventTypes)) {
                            if (event.getEventTypes().contains("select")) {
                                getTabs().stream()
                                        .filter(tab -> tab.getUserData() == event.getData())
                                        .findFirst()
                                        .ifPresent(tab -> getSelectionModel().select(tab));
                            } else if (event.getEventTypes().contains("open")) {
                                // if already open, select tab; else, create new editor tab
                                getTabs().stream()
                                        .filter(tab -> tab.getUserData() == event.getData())
                                        .findFirst()
                                        .ifPresentOrElse(tab -> getSelectionModel().select(tab), () -> {
                                            List<EditorProviderService> supportedEditors = editorProviderServices.stream()
                                                    .filter(editorProviderService -> editorProviderService.supports(event.getData()))
                                                    .collect(Collectors.toList());
                                            if (!supportedEditors.isEmpty()) {
                                                Tab tab = createTab(event.getData(), supportedEditors);
                                                getTabs().add(tab);
                                                getSelectionModel().select(tab);
                                            }
                                        });
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });

        // deserialize tabs using providers

        // serialize tabs on change
    }

    private Tab createTab(Object data, List<EditorProviderService> editorProviders) {
        // TODO: listener to populate additional supporting editors
        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.BOTTOM);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        System.out.println(editorProviders.size());
        editorProviders.forEach(editorProviderService -> System.out.println(editorProviderService.getName()));
        List<Tab> viewTabs = editorProviders.stream()
                .map(editorProviderService -> editorProviderService.create(workspace, data))
                .filter(Objects::nonNull)
                .map(this::createViewTab)
                .collect(Collectors.toList());
        System.out.println(viewTabs.size());
        tabPane.getTabs().addAll(viewTabs);

        Tab tab = new Tab();
        tab.setContent(tabPane);
        tab.setUserData(data);
        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Editor editor = (Editor) newValue.getUserData();
                tab.setText(editor.getDisplayName());
                editor.displayNameProperty().addListener((obs, oldVal, newVal) -> {
                    if (editor.equals(getSelectionModel().getSelectedItem().getUserData())) {
                        tab.setText(newVal);
                    }
                });
            }
        });
        tab.textProperty().bind(Bindings.createStringBinding(() -> ((Editor) tabPane.getSelectionModel().getSelectedItem().getUserData()).getDisplayName(),
                tabPane.getSelectionModel().selectedItemProperty()));
        return tab;
    }

    private Tab createViewTab(Editor editor) {
        Tab tab = new Tab(editor.getName());
        tab.setContent(editor.getNode());
        tab.setUserData(editor);
        return tab;
    }
}
