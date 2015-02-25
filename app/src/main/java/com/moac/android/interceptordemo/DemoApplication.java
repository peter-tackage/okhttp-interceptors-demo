package com.moac.android.interceptordemo;

import com.facebook.stetho.Stetho;
import com.moac.android.interceptordemo.injection.InjectingApplication;

import java.util.Arrays;
import java.util.List;

public class DemoApplication extends InjectingApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }

    @Override
    public List<Object> getModules() {
        List<Object> modules = super.getModules();
        modules.addAll(Arrays.asList(Modules.appList(this)));
        return modules;
    }
}
