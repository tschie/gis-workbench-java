package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.view;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.Event;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.event.service.EventService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service.Editor;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.editor.service.EditorProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace.Workspace;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import org.reactivestreams.FlowAdapters;
import io.reactivex.processors.FlowableProcessor;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
                .onBackpressureDrop()
                .subscribe(event -> {
                    try {
                        if (event.getWorkspace().getRootDirectory() == workspace.getRootDirectory() && Collections.disjoint(event.getEventTypes(), eventTypes)) {
                            if (event.getEventTypes().contains("select")) {
                                Optional<Tab> matchingTab = getTabs().stream()
                                        .filter(tab -> tab.getUserData() == event.getData())
                                        .findFirst();
                                matchingTab.ifPresent(tab -> getSelectionModel().select(tab));
                            } else if (event.getEventTypes().contains("open")) {
                                List<EditorProviderService> supportedEditors = editorProviderServices.stream()
                                        .filter(editorProviderService -> editorProviderService.supports(event.getData()))
                                        .collect(Collectors.toList());
                                if (!supportedEditors.isEmpty()) {
                                    Tab tab = createTab(event.getData(), editorProviderServices);
                                    getTabs().add(tab);
                                    getSelectionModel().select(tab);
                                }
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
        // create tab with all supporting editors
        // TODO: listener to populate additional supporting editors
        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.BOTTOM);
        tabPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        List<Tab> viewTabs = editorProviderServices.stream()
                .map(editorProviderService -> editorProviderService.create(workspace, null, data))
                .filter(Objects::nonNull)
                .map(this::createViewTab)
                .collect(Collectors.toList());
        tabPane.getTabs().addAll(viewTabs);
        tabPane.getSelectionModel().select(viewTabs.get(0));

        Tab tab = new Tab();
        tab.setContent(tabPane);
        tab.setUserData(data);
        tab.textProperty().bind(Bindings.createStringBinding(() -> tabPane.getSelectionModel().getSelectedItem().getText(), tabPane.getSelectionModel().selectedItemProperty()));
        return tab;
    }

    private Tab createViewTab(Editor editor) {
        Tab tab = new Tab(editor.getDisplayName());
        tab.setContent(editor.getNode());
        tab.setUserData(editor);
        return tab;
    }
}
