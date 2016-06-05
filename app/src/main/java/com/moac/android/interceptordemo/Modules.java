package com.moac.android.interceptordemo;

import com.moac.android.interceptordemo.injection.module.AppModule;
import com.moac.android.interceptordemo.injection.module.UiModule;

import android.app.Activity;
import android.app.Application;

public class Modules {

    // Defines all modules used by the app
    public static Object[] appList(Application app) {
        return new Object[]{new AppModule(app)};
    }

    // Defines all modules used by the UI
    public static Object[] uiList(Activity activity) {
        return new Object[]{new UiModule(activity)};
    }

    private Modules() {
        // No instances.
    }
}
