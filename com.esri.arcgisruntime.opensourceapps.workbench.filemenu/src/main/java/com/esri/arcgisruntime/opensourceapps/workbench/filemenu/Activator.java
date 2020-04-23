package com.esri.arcgisruntime.opensourceapps.workbench.filemenu;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("File menu started");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("File menu stopped");
    }
}
