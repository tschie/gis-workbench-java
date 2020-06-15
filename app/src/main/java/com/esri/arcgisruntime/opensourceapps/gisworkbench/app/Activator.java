/*
 * Copyright 2020 Esri.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.esri.arcgisruntime.opensourceapps.gisworkbench.app;

import javafx.application.Platform;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Activator implements BundleActivator {
    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("GIS Workbench started");
        // start JavaFX if not already started
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Platform.startup(countDownLatch::countDown);
        countDownLatch.await(10, TimeUnit.SECONDS);

        SplashScreen splashScreen = SplashScreen.getSplashScreen();
        if (splashScreen != null) {
            splashScreen.close();
        }
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        System.out.println("GIS Workbench stopped");
    }
}
