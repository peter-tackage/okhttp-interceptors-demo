package com.moac.android.interceptordemo.injection.module;

import android.util.Log;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.moac.android.interceptordemo.network.LoggingInterceptor;
import com.squareup.okhttp.Interceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;

/*
 * Provides implementation of logging handlers
 */
@Module(library = true)
public class LoggingModule {

    @Provides
    @Singleton
    ErrorHandler provideErrorHandler() {
        return ErrorHandler.DEFAULT;
    }

    @Provides
    @Singleton
    @NetworkModule.AppLogging
    Interceptor provideAppInterceptor() {
        return new LoggingInterceptor("HTTP|SoundCloud");
    }

    @Provides
    @Singleton
    @NetworkModule.NetworkLogging
    Interceptor provideNetworkInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @Singleton
    RestAdapter.Log provideLogger() {
        return new RestAdapter.Log() {
            public void log(String msg) {
                Log.i("Retrofit|SoundCloud", msg);
            }
        };
    }
}
