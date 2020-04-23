package com.esri.arcgisruntime.opensourceapps.workbench.filetab;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.osgi.framework.FrameworkUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileTab extends Tab {

    @FXML
    private TextFlow textFlow;

    private final File file;

    public FileTab(File file) {
        super();

        this.file = file;

        FXMLLoader loader = new FXMLLoader(FrameworkUtil.getBundle(getClass()).getResource("/fxml/fileTab.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                textFlow.getChildren().addAll(new Text(line), new Text(System.lineSeparator()));
            }
            setText(file.getName());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
