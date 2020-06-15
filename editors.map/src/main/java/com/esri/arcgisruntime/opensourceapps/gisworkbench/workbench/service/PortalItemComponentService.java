package com.esri.arcgisruntime.opensourceapps.gisworkbench.workbench.service;

import com.esri.arcgisruntime.opensourceapps.workbench.core.repository.MutableKeyedRepository;
import com.esri.arcgisruntime.opensourceapps.workbench.core.repository.ObservableKeyedRepository;
import com.esri.arcgisruntime.portal.PortalItem;

import java.util.UUID;

public interface PortalItemComponentService extends ObservableKeyedRepository<UUID, PortalItem>,
        MutableKeyedRepository<UUID, PortalItem> {
}
