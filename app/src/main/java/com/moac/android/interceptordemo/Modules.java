package com.moac.android.interceptordemo;

import android.app.Activity;
import android.app.Application;

import com.moac.android.interceptordemo.module.AppModule;
import com.moac.android.interceptordemo.module.UiModule;

public class Modules {

    // Defines all modules used by the app
    public static Object[] appList(Application app) {
        return new Object[]{
                new AppModule(app)
        };
    }

    // Defines all modules used by the UI
    public static Object[] uiList(Activity activity) {
        return new Object[]{
                new UiModule(activity)
        };
    }


    private Modules() {
        // No instances.
    }
}
