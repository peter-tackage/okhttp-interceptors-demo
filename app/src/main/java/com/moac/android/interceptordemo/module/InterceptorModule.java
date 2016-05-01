package com.moac.android.interceptordemo.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.moac.android.interceptordemo.interceptor.BandwidthLimitingInterceptor;
import com.moac.android.interceptordemo.interceptor.LoggingInterceptor;
import com.moac.android.interceptordemo.interceptor.NeverCacheInterceptor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;

/*
 * Provides implementation of interceptors
 */
@Module(library = true, complete = false)
public class InterceptorModule {

    static final String BANDWIDTH_LIMIT_IN_BYTES = "BandwidthLimitInBytes";

    @Provides
    @NetworkModule.AppInterceptors
    List<Interceptor> provideAppInterceptors() {
        // Comment/uncomment to experiment
        ArrayList<Interceptor> appInterceptors = new ArrayList<>();
        appInterceptors.add(new LoggingInterceptor("HTTP|SoundCloud"));
        //  appInterceptors.add(new AssertNoCacheInterceptor());
        return appInterceptors;
    }

    @Provides
    @NetworkModule.NetworkInterceptors
    List<Interceptor> provideNetworkInterceptors(StethoInterceptor stethoInterceptor) {
        // Comment/uncomment to experiment
        ArrayList<Interceptor> networkInterceptors = new ArrayList<>();
        //   networkInterceptors.add(new AssertNoCacheInterceptor());
        networkInterceptors.add(stethoInterceptor);
        networkInterceptors.add(new NeverCacheInterceptor());
        return networkInterceptors;
    }

    @Provides
    @Singleton
    StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @Singleton
    BandwidthLimitingInterceptor provideBandwidthLimitingInterceptor(
            @Named(BANDWIDTH_LIMIT_IN_BYTES) long bandwidthLimitInBytes) {
        // enforces limit for all OkHttp clients
        return new BandwidthLimitingInterceptor(bandwidthLimitInBytes);
    }

}
