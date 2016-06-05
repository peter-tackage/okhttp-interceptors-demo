package com.moac.android.interceptordemo.injection.module;

import com.moac.android.interceptordemo.config.DebugConfigurationProvider;
import com.moac.android.interceptordemo.config.IDebugConfigurationProvider;

import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class DebugModule {

    @Singleton
    @Provides
    IDebugConfigurationProvider provideDebugConfigurationProvider(
            SharedPreferences sharedPreferences) {
        return new DebugConfigurationProvider(sharedPreferences);
    }
}
