package com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.files;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Files perspective started");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Files perspective stopped");
    }
}
