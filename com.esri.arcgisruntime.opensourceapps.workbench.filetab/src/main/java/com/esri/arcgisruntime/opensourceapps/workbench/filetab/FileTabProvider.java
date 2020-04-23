package com.esri.arcgisruntime.opensourceapps.workbench.filetab;

import com.esri.arcgisruntime.opensourceapps.workbench.project.service.TabService;
import com.esri.arcgisruntime.opensourceapps.workbench.selection.service.SelectionService;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.File;

@Component(immediate = true)
public class FileTabProvider {

    @Reference
    private SelectionService selectionService;

    @Reference
    private TabService tabService;

    @Activate
    private void activate() {
        selectionService.selectionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue instanceof File && ((File) newValue).isFile()) {
                tabService.addTab(new FileTab((File) newValue));
            }
        });
    }
}
