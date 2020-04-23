package com.esri.arcgisruntime.opensourceapps.workbench.perspective.service;

import javafx.beans.property.*;
import javafx.scene.Node;

public class Perspective {
    private final ReadOnlyStringWrapper name;
    private final ReadOnlyObjectWrapper<Node> root;

    public Perspective(String name, Node root) {
        this.name = new ReadOnlyStringWrapper(name);
        this.root = new ReadOnlyObjectWrapper<>(root);
    }

    public String getName() {
        return nameProperty().get();
    }

    public ReadOnlyStringWrapper nameProperty() {
        return name;
    }

    public Node getRoot() {
        return rootProperty().get();
    }

    public ReadOnlyObjectWrapper<Node> rootProperty() {
        return root;
    }

    @Override
    public String toString() {
        return getName();
    }
}
