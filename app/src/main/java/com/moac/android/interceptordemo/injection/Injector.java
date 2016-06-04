package com.moac.android.interceptordemo.injection;

import android.support.annotation.NonNull;

import dagger.ObjectGraph;

public interface Injector {

    ObjectGraph getObjectGraph();

    void inject(@NonNull final Object target);
}

