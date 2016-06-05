package com.moac.android.interceptordemo.injection.module;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class PersistenceModule {

    @Singleton
    @Provides
    SharedPreferences provideSharedPreferences(@AppModule.ForApplication Context context) {
        return context.getSharedPreferences("config", Context.MODE_PRIVATE);
    }
}
