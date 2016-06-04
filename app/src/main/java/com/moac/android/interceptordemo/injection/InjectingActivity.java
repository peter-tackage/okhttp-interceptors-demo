package com.moac.android.interceptordemo.injection;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

public abstract class InjectingActivity extends AppCompatActivity implements Injector {

    private ObjectGraph mObjectGraph;

    @Override
    public final ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }

    @Override
    public void inject(@NonNull final Object target) {
        mObjectGraph.inject(target);
    }

    @Override
    protected void onCreate(final android.os.Bundle savedInstanceState) {
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