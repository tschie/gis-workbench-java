package com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.maps;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Maps perspective started");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Maps perspective stopped");
    }
}
