package com.moac.android.interceptordemo.injection;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

public class InjectingFragment extends Fragment implements Injector {
    private ObjectGraph mObjectGraph;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (mObjectGraph == null) {
            ObjectGraph appGraph = ((Injector) getActivity()).getObjectGraph();
            List<Object> fragmentModules = getModules();
            mObjectGraph = appGraph.plus(fragmentModules.toArray());
            inject(this);
        }
    }

    @Override
    public void onDestroy() {
        mObjectGraph = null;
        super.onDestroy();
    }

    @Override
    public final ObjectGraph getObjectGraph() {
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
