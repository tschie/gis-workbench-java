package com.esri.arcgisruntime.opensourceapps.gisworkbench.event;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Event started");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Event stopped");
    }
}
