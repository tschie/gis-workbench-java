package com.esri.arcgisruntime.opensourceapps.workbench.preferences.impl;

import com.esri.arcgisruntime.opensourceapps.workbench.preferences.Activator;
import com.esri.arcgisruntime.opensourceapps.workbench.preferences.service.PreferencesService;
import org.osgi.service.component.annotations.Component;

import java.util.prefs.Preferences;

@Component(immediate = true)
public class PreferencesServiceImpl implements PreferencesService {

    private final Preferences preferences = Preferences.userRoot().node(Activator.class.getName());

    @Override
    public Preferences getPreferences() {
        return preferences;
    }
}
