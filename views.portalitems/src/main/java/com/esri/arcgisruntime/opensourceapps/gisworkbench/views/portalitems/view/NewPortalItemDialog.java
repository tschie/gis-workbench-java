package com.esri.arcgisruntime.opensourceapps.gisworkbench.views.portalitems.view;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.opensourceapps.gisworkbench.views.portalitems.Activator;
import com.esri.arcgisruntime.portal.Portal;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import org.osgi.framework.FrameworkUtil;

/**
 * Dialog for returning a portal item based on a portal URL and item ID.
 */
public class NewPortalItemDialog extends Dialog<PortalItem> {

  @FXML
  private TextField portalUrlTextField;

  @FXML
  private TextField itemIdTextField;

  @FXML
  private ButtonType okButtonType;

  private final BooleanProperty valid = new SimpleBooleanProperty(false);

  /**
   * Creates a new dialog.
   */
  public NewPortalItemDialog() {

    // create the dialog from FXML
    FXMLLoader fxmlLoader = new FXMLLoader();
    fxmlLoader.setClassLoader(Activator.class.getClassLoader());
    fxmlLoader.setLocation(FrameworkUtil.getBundle(Activator.class).getResource("/fxml/newPortalDialog.fxml"));
    fxmlLoader.setRoot(this);
    fxmlLoader.setController(this);

    try {
      fxmlLoader.load();
    } catch (Exception e) {
      e.printStackTrace();
    }

    // validate the portal URL
    portalUrlTextField.focusedProperty().addListener(observable -> validate());

    // validate the item id
    itemIdTextField.focusedProperty().addListener(observable -> validate());

    // enable the OK button when both fields are valid
    getDialogPane().lookupButton(okButtonType).disableProperty().bind(valid.not());

    // return the portal item
    setResultConverter(dialogButton -> {
      if (dialogButton == okButtonType) {
        return new PortalItem(new Portal(portalUrlTextField.getText()), itemIdTextField.getText());
      }
      return null;
    });
  }

  /**
   * Validate that the text field inputs can create a valid portal item.
   */
  private void validate() {
    if (portalUrlTextField.getText() == null || itemIdTextField.getText() == null) {
      valid.set(false);
    } else {
      try {
        Portal portal = new Portal(portalUrlTextField.getText());
        PortalItem portalItem = new PortalItem(portal, itemIdTextField.getText());
        portalItem.loadAsync();
        portalItem.addDoneLoadingListener(() -> valid.set(portalItem.getLoadStatus() == LoadStatus.LOADED));
      } catch (Exception ex) {
        valid.set(false);
      }
    }
  }
}
