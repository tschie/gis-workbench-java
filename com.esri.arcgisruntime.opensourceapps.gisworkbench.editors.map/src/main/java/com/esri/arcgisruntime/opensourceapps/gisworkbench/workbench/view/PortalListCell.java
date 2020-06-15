package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.view;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.portal.Portal;
import javafx.scene.control.ListCell;

public class PortalListCell extends ListCell<Portal> {

    public PortalListCell() {
        super();
    }

    @Override
    protected void updateItem(Portal portal, boolean b) {
        super.updateItem(portal, b);
        if (!b && portal != null) {
            if (portal.getLoadStatus() == LoadStatus.LOADED) {
                setText(portal.getPortalInfo().getPortalName() + " - " + portal.getUri());
                System.out.println(portal.getPortalInfo().getPortalThumbnailFileName());
            } else {
                portal.addDoneLoadingListener(() -> updateItem(portal, false));
                setText(portal.getUri());
            }
        } else {
            setText(null);
        }
        setGraphic(null);
    }
}
