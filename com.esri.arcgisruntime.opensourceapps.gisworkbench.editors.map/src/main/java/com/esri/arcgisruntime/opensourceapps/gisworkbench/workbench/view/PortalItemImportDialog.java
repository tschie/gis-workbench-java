package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.view;

import com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.Activator;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.osgi.framework.FrameworkUtil;

import java.io.IOException;

public class PortalItemImportDialog extends Dialog<PortalItem> {

    @FXML
    private ComboBox<Portal> portalComboBox;

    @FXML
    private TextField portalItemTextField;

    @FXML
    private ButtonType okButtonType;

    public PortalItemImportDialog() {
        super();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            fxmlLoader.setClassLoader(Activator.class.getClassLoader());
            fxmlLoader.setLocation(FrameworkUtil.getBundle(Activator.class).getResource("/fxml/portalItemDialog.fxml"));
            fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        portalComboBox.getItems().add(new Portal("https://www.arcgis.com"));
        portalComboBox.getSelectionModel().select(0);
        portalComboBox.setCellFactory(c -> new PortalListCell());
        portalComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Portal portal) {
                return portal.getUri();
            }

            @Override
            public Portal fromString(String s) {
                return new Portal(s);
            }
        });

        portalItemTextField.disableProperty().bind(portalComboBox.getSelectionModel().selectedItemProperty().isNull());

        getDialogPane().lookupButton(okButtonType).disableProperty().bind(portalItemTextField.textProperty().isNull().or(portalItemTextField.textProperty().isEmpty()));

        setResultConverter(buttonType -> {
            if (buttonType.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                return new PortalItem(portalComboBox.getSelectionModel().getSelectedItem(), portalItemTextField.getText());
            } else {
                return null;
            }
        });
    }
}
