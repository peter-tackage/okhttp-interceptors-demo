package com.moac.android.interceptordemo.injection;

import dagger.ObjectGraph;

public interface Injector {

    ObjectGraph getObjectGraph();

    void inject(Object target);
}

