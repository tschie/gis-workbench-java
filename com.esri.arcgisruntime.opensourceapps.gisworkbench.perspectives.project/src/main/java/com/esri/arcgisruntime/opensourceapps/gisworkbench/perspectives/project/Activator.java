package com.esri.arcgisruntime.opensourceapps.gisworkbench.perspectives.project;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Project perspective started");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Project perspective stopped");
    }
}
