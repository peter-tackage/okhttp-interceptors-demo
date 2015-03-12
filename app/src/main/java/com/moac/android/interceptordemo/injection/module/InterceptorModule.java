package com.moac.android.interceptordemo.injection.module;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.moac.android.interceptordemo.network.AssertNoCacheInterceptor;
import com.moac.android.interceptordemo.network.LoggingInterceptor;
import com.moac.android.interceptordemo.network.NeverCacheInterceptor;
import com.squareup.okhttp.Interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/*
 * Provides implementation of interceptors
 */
@Module(library = true)
public class InterceptorModule {

    @Provides
    @Singleton
    @NetworkModule.AppInterceptors
    List<Interceptor> provideAppInterceptors() {
        ArrayList<Interceptor> appInterceptors = new ArrayList<>();
        appInterceptors.add(new LoggingInterceptor("HTTP|SoundCloud"));
        // Uncomment to test cache header removed by Network Interceptors
        appInterceptors.add(new AssertNoCacheInterceptor());
        return appInterceptors;
    }

    @Provides
    @Singleton
    @NetworkModule.NetworkInterceptors
    List<Interceptor> provideNetworkInterceptors(StethoInterceptor stethoInterceptor) {
        ArrayList<Interceptor> networkInterceptors = new ArrayList<>();
        // Uncomment this to remove caching
        networkInterceptors.add(new AssertNoCacheInterceptor());
        networkInterceptors.add(new NeverCacheInterceptor());
        networkInterceptors.add(stethoInterceptor);
        return networkInterceptors;
    }

    @Provides
    @Singleton
    StethoInterceptor provideStethoInterceptor() {
        // I think this MUST be a singleton. I've had trouble running with multiple instances
        return new StethoInterceptor();
    }

}
