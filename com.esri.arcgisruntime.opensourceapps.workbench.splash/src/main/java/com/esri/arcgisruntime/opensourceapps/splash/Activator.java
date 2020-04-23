package com.esri.arcgisruntime.opensourceapps.splash;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.awt.*;

public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Splash started");
        // close splash screen if there is one
        SplashScreen splashScreen = SplashScreen.getSplashScreen();
        if (splashScreen != null) {
            splashScreen.close();
        }
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("Splash stopped");
    }
}
