package com.moac.android.interceptordemo.injection;

import dagger.ObjectGraph;

public interface Injector {
    public ObjectGraph getObjectGraph();

    public void inject(Object target);
}

