package com.esri.arcgisruntime.opensourceapps.workbench.menu;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Menu started");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Menu stopped");
    }
}
