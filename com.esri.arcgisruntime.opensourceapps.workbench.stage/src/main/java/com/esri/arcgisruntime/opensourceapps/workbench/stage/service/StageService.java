package com.esri.arcgisruntime.opensourceapps.workbench.stage.service;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.stage.Stage;

public interface StageService {
  Stage getStage();
  ReadOnlyObjectProperty<Stage> stageProperty();
}
