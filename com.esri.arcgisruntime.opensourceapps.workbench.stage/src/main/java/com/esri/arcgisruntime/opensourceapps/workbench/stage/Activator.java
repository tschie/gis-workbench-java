package com.esri.arcgisruntime.opensourceapps.workbench.stage;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Stage started");
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Stage stopped");
    }
}
