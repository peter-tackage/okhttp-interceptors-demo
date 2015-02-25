package com.moac.android.interceptordemo.activity;

import android.os.Bundle;

import com.moac.android.interceptordemo.Modules;
import com.moac.android.interceptordemo.R;
import com.moac.android.interceptordemo.injection.InjectingActivity;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends InjectingActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public List<Object> getModules() {
        List<Object> modules = super.getModules();
        modules.addAll(Arrays.asList(Modules.uiList(this)));
        return modules;
    }
}
