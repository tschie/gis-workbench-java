package com.esri.arcgisruntime.opensourceapps.gisworkbench.workspace;

import java.io.File;

public class Workspace {
    private final File rootDirectory;
    private final File metadataDirectory;

    public Workspace(File rootDirectory, File metadataDirectory) {
        this.rootDirectory = rootDirectory;
        this.metadataDirectory = metadataDirectory;
    }

    public File getRootDirectory() {
        return rootDirectory;
    }

    public File getMetadataDirectory() {
        return metadataDirectory;
    }
}
