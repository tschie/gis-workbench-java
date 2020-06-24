package com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.impl;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service.EditorProviderService;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.editor.service.EditorService;
import javafx.beans.property.ReadOnlyListProperty;
import javafx.beans.property.ReadOnlyListWrapper;
import javafx.collections.FXCollections;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

import java.util.concurrent.CopyOnWriteArrayList;

@Component(immediate = true)
public class EditorServiceImpl implements EditorService {

  private final ReadOnlyListWrapper<EditorProviderService> editorProviderServices = new ReadOnlyListWrapper<>(
      FXCollections.observableArrayList(new CopyOnWriteArrayList<>())
  );

  @Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
  public void addEditorProviderService(EditorProviderService editorProviderService) {
    editorProviderServices.add(editorProviderService);
  }

  public void removeEditorProviderService(EditorProviderService editorProviderService) {
    editorProviderServices.remove(editorProviderService);
  }

  @Override
  public ReadOnlyListProperty<EditorProviderService> observableListProperty() {
    return editorProviderServices.getReadOnlyProperty();
  }
}
