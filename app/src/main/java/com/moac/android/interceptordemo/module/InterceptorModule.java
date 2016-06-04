package com.moac.android.interceptordemo.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.moac.android.interceptordemo.interceptor.LoggingInterceptor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;

/*
 * Provides implementation of interceptors
 */
@Module(library = true, complete = false)
public class InterceptorModule {

    static final String API_CLIENT_ID = "ApiClientId";

    @Provides
    @NetworkModule.AppInterceptors
    List<Interceptor> provideAppInterceptors() {
        ArrayList<Interceptor> appInterceptors = new ArrayList<>();
        appInterceptors.add(new LoggingInterceptor("HTTP|SoundCloud"));
        return appInterceptors;
    }

    @Provides
    @NetworkModule.NetworkInterceptors
    List<Interceptor> provideNetworkInterceptors(StethoInterceptor stethoInterceptor) {
        ArrayList<Interceptor> networkInterceptors = new ArrayList<>();
        networkInterceptors.add(stethoInterceptor);
        return networkInterceptors;
    }

    @Provides
    @Singleton
    StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }


}
