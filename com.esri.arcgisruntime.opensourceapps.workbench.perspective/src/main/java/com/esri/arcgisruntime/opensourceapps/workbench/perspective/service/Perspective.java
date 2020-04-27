/*
 * Copyright 2020 Esri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.esri.arcgisruntime.opensourceapps.workbench.perspective.service;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
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
