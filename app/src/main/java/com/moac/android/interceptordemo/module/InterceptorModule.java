package com.moac.android.interceptordemo.module;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.moac.android.interceptordemo.interceptor.AssertNoCacheInterceptor;
import com.moac.android.interceptordemo.interceptor.BandwidthLimitingInterceptor;
import com.moac.android.interceptordemo.interceptor.LoggingInterceptor;
import com.moac.android.interceptordemo.interceptor.NeverCacheInterceptor;
import com.squareup.okhttp.Interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
    List<Interceptor> provideNetworkInterceptors(BandwidthLimitingInterceptor bandwidthLimitingInterceptor,
                                                 StethoInterceptor stethoInterceptor) {
        // Comment/uncomment to experiment
        ArrayList<Interceptor> networkInterceptors = new ArrayList<>();
        networkInterceptors.add(bandwidthLimitingInterceptor);
     //   networkInterceptors.add(new AssertNoCacheInterceptor());
        networkInterceptors.add(stethoInterceptor);
        networkInterceptors.add(new NeverCacheInterceptor());
        return networkInterceptors;
    }

    @Provides
    @Singleton
    StethoInterceptor provideStethoInterceptor() {
        // I think this MUST be a singleton. I've had trouble running with multiple instances
        return new StethoInterceptor();
    }

    @Provides
    @Singleton // enforce limit for all OkHttp clients
    BandwidthLimitingInterceptor provideBandwidthLimitingInterceptor(@Named(BANDWIDTH_LIMIT_IN_BYTES) long bandwidthLimitInBytes) {
        return new BandwidthLimitingInterceptor(bandwidthLimitInBytes);
    }

}
