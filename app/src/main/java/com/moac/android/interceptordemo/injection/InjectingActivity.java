package com.moac.android.interceptordemo.injection;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

public abstract class InjectingActivity extends Activity implements Injector {

    private ObjectGraph mObjectGraph;

    @Override
    public final ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }

    @Override
    public void inject(Object target) {
        mObjectGraph.inject(target);
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mObjectGraph = ((Injector) getApplication()).getObjectGraph().plus(getModules().toArray());
        inject(this);
    }

    @Override
    protected void onDestroy() {
        mObjectGraph = null;
        super.onDestroy();
    }

    protected List<Object> getModules() {
        return new ArrayList<>();
    }

}