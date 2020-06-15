package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.view;

import com.esri.arcgisruntime.loadable.LoadStatus;
import com.esri.arcgisruntime.portal.PortalItem;
import javafx.scene.control.ListCell;

import java.util.Arrays;

public class PortalItemListCell extends ListCell<PortalItem> {

    public PortalItemListCell() {
        super();
    }

    @Override
    protected void updateItem(PortalItem portalItem, boolean b) {
        super.updateItem(portalItem, b);
        if (!b && portalItem != null) {
            if (portalItem.getLoadStatus() == LoadStatus.LOADED) {
                setText(portalItem.getTitle());
                System.out.println(portalItem.getThumbnailFileName());
                System.out.println(Arrays.toString(portalItem.getThumbnailData()));
            } else {
                portalItem.addDoneLoadingListener(() -> updateItem(portalItem, false));
                setText(portalItem.getItemId());
            }
        } else {
            setText(null);
        }
        setGraphic(null);
    }
}
