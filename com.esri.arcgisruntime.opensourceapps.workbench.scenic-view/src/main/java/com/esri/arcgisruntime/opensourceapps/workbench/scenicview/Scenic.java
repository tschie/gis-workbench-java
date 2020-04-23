package com.esri.arcgisruntime.opensourceapps.workbench.scenicview;

import com.esri.arcgisruntime.opensourceapps.workbench.stage.service.StageService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.scenicview.ScenicView;

@Component(immediate = true)
public class Scenic {

    @Reference
    private StageService stageService;

    @Activate
    private void activate() {
        if (stageService.getStage() != null) {
            ScenicView.show(stageService.getStage().getScene());
        } else {
            stageService.stageProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    ScenicView.show(stageService.getStage().getScene());
                }
            });
        }
    }
}
