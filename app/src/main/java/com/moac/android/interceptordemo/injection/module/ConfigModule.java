package com.moac.android.interceptordemo.injection.module;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/*
 * Provides required configuration settings for the modules
 */
@Module(library = true)
public class ConfigModule {

    // API Configuration //

    @Provides
    @Singleton
    @Named(InterceptorModule.API_CLIENT_ID)
    String provideClientId() {
        //throw new IllegalStateException("You need to provide your own SoundCloud API client id");
        return "0a1157d1c838727363c4e01cdace18bc";
    }

    @Provides
    @Singleton
    @Named(ApiModule.API_ENDPOINT_URL)
    String provideEndpointUrl() {
        return "https://api.soundcloud.com";
    }

    @Provides
    @Singleton
    @Named(NetworkModule.API_CACHE_NAME)
    String provideApiCacheName() {
        return "http";
    }

    @Provides
    @Singleton
    @Named(NetworkModule.API_CACHE_SIZE_IN_BYTES)
    long provideApiCacheSizeMegaBytes() {
        return 50 * 1024 * 1024; // 50MB
    }

    // Images Configuration //

    @Provides
    @Singleton
    @Named(NetworkModule.IMAGES_CACHE_NAME)
    String provideImagesCacheName() {
        return "images";
    }

    @Provides
    @Singleton
    @Named(NetworkModule.IMAGES_CACHE_SIZE_IN_BYTES)
    long provideImagesCacheSizeMegaBytes() {
        return 10 * 1024 * 1024; // 10MB
    }

}
