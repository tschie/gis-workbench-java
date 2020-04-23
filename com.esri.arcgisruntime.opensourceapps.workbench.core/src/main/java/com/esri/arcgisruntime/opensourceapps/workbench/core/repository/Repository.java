package com.esri.arcgisruntime.opensourceapps.workbench.core.repository;

import java.util.List;

public interface Repository<T> {
    List<T> getAll();
}
