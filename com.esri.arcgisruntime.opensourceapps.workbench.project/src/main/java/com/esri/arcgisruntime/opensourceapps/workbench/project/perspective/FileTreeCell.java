package com.esri.arcgisruntime.opensourceapps.workbench.project.perspective;

import com.esri.arcgisruntime.opensourceapps.workbench.project.Activator;
import javafx.scene.control.TreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.osgi.framework.FrameworkUtil;

import java.io.File;
import java.io.IOException;

public class FileTreeCell extends TreeCell<File> {

    @Override
    protected void updateItem(File file, boolean b) {
        super.updateItem(file, b);
        if (!b && file != null) {
            setText(file.getName());
            ImageView imageView = new ImageView();
            setGraphic(imageView);
            try {
                if (file.isDirectory()) {
                    imageView.setImage(new Image(FrameworkUtil.getBundle(Activator.class).getResource(
                            "/images/folder-outline.png").openStream(), 14, 14, true, true));
                } else {
                    imageView.setImage(new Image(FrameworkUtil.getBundle(Activator.class).getResource(
                            "/images/file-outline.png").openStream(), 14, 14, true, true));
                }
            } catch (IOException ex) {
                // do nothing
            }
        } else {
            setText(null);
            setGraphic(null);
        }
    }
}
