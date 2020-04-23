package com.esri.arcgisruntime.opensourceapps.workbench.selection.service;

import javafx.beans.property.ReadOnlyObjectProperty;

public interface SelectionService {
  Object getSelection();
  void setSelection(Object object);
  ReadOnlyObjectProperty<Object> selectionProperty();
}
