package com.moac.android.interceptordemo.injection;

import android.app.Application;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

public abstract class InjectingApplication extends Application implements Injector {

    ObjectGraph mObjectGraph;

    @Override
    public void onCreate() {
        super.onCreate();
        mObjectGraph = ObjectGraph.create(getModules().toArray());
        mObjectGraph.inject(this);
    }

    @Override
    public ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }

    @Override
    public void inject(@NonNull final Object target) {
        mObjectGraph.inject(target);
    }

    protected List<Object> getModules() {
        return new ArrayList<>();
    }

}
