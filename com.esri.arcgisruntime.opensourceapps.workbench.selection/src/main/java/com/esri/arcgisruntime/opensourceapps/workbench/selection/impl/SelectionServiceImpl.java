package com.esri.arcgisruntime.opensourceapps.workbench.selection.impl;

import com.esri.arcgisruntime.opensourceapps.workbench.selection.service.SelectionService;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import org.osgi.service.component.annotations.Component;

@Component(immediate = true)
public class SelectionServiceImpl implements SelectionService {

    private final ReadOnlyObjectWrapper<Object> selection = new ReadOnlyObjectWrapper<>(null);

    @Override
    public Object getSelection() {
        return selectionProperty().get();
    }

    @Override
    public void setSelection(Object object) {
        selection.set(object);
    }

    @Override
    public ReadOnlyObjectProperty<Object> selectionProperty() {
        return selection.getReadOnlyProperty();
    }
}
