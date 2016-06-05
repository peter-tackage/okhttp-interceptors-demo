package com.moac.android.interceptordemo.injection.module;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.moac.android.interceptordemo.config.IDebugConfigurationProvider;
import com.moac.android.interceptordemo.interceptor.DelayDebugInterceptor;
import com.moac.android.interceptordemo.interceptor.LargeBodyWarningLoggingInterceptor;
import com.moac.android.interceptordemo.interceptor.LoggingInterceptor;
import com.moac.android.interceptordemo.interceptor.NetworkErrorDebugInterceptor;
import com.moac.android.interceptordemo.interceptor.NoStoreCacheEnforcingInterceptor;
import com.moac.android.interceptordemo.interceptor.ServerErrorDebugInterceptor;
import com.moac.android.interceptordemo.interceptor.SoundCloudRequestInterceptor;

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

    private static final String LOGGING_TAG = "HTTP|SoundCloud";

    static final String API_CLIENT_ID = "ApiClientId";

    @Provides
    @NetworkModule.AppInterceptors
    List<Interceptor> provideAppInterceptors(
            final SoundCloudRequestInterceptor soundCloudRequestInterceptor,
            final LoggingInterceptor loggingInterceptor,
            final DelayDebugInterceptor delayDebugInterceptor) {
        ArrayList<Interceptor> appInterceptors = new ArrayList<>();
        appInterceptors.add(soundCloudRequestInterceptor);
        appInterceptors.add(loggingInterceptor);
        appInterceptors.add(delayDebugInterceptor);
        return appInterceptors;
    }

    @Provides
    @NetworkModule.NetworkInterceptors
    List<Interceptor> provideNetworkInterceptors(final StethoInterceptor stethoInterceptor,
                                                 final NoStoreCacheEnforcingInterceptor noStoreCacheEnforcingInterceptor,
                                                 final ServerErrorDebugInterceptor serverErrorDebugInterceptor,
                                                 final NetworkErrorDebugInterceptor networkErrorDebugInterceptor,
                                                 final LargeBodyWarningLoggingInterceptor largeBodyWarningLoggingInterceptor) {
        ArrayList<Interceptor> networkInterceptors = new ArrayList<>();
        networkInterceptors.add(stethoInterceptor);
        networkInterceptors.add(noStoreCacheEnforcingInterceptor);
        networkInterceptors.add(serverErrorDebugInterceptor);
        networkInterceptors.add(networkErrorDebugInterceptor);
        networkInterceptors.add(largeBodyWarningLoggingInterceptor);
        return networkInterceptors;
    }

    @Provides
    @Singleton
    StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @Singleton
    NoStoreCacheEnforcingInterceptor provideNoStoreCacheEnforcingInterceptor(
            IDebugConfigurationProvider debugConfigurationProvider) {
        return new NoStoreCacheEnforcingInterceptor(debugConfigurationProvider);
    }

    @Provides
    @Singleton
    ServerErrorDebugInterceptor provideServerErrorDebugInterceptor(
            IDebugConfigurationProvider debugConfigurationProvider) {
        return new ServerErrorDebugInterceptor(debugConfigurationProvider, 404,
                                               "{ error : \"fake\" }");
    }

    @Provides
    @Singleton
    NetworkErrorDebugInterceptor provideNetworkErrorDebugInterceptor(
            IDebugConfigurationProvider debugConfigurationProvider) {
        return new NetworkErrorDebugInterceptor(debugConfigurationProvider);
    }

    @Provides
    @Singleton
    LargeBodyWarningLoggingInterceptor provideLargeBodyWarningLoggingInterceptor(
            IDebugConfigurationProvider debugConfigurationProvider) {
        return new LargeBodyWarningLoggingInterceptor(LOGGING_TAG, debugConfigurationProvider);
    }

    @Provides
    @Singleton
    LoggingInterceptor provideLoggingInterceptor() {
        return new LoggingInterceptor(LOGGING_TAG);
    }

    @Provides
    @Singleton
    DelayDebugInterceptor provideDelayDebugInterceptor(
            IDebugConfigurationProvider debugConfigurationProvider) {
        return new DelayDebugInterceptor(debugConfigurationProvider);
    }

    @Provides
    @Singleton
    SoundCloudRequestInterceptor provideSoundCloudRequestInterceptor(
            @Named(InterceptorModule.API_CLIENT_ID) String clientId) {
        return new SoundCloudRequestInterceptor(clientId);
    }
}
