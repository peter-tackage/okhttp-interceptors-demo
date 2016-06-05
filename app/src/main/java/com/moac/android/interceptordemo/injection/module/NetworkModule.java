package com.moac.android.interceptordemo.injection.module;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.util.List;

import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Module(complete = false, library = true)
public class NetworkModule {

    private static final String TAG = NetworkModule.class.getSimpleName();

    // Required configuration settings definitions //

    static final String API_CACHE_NAME = "ApiCacheName";
    static final String API_CACHE_SIZE_IN_BYTES = "ApiCacheSizeInBytes";
    static final String IMAGES_CACHE_NAME = "ImagesCacheName";
    static final String IMAGES_CACHE_SIZE_IN_BYTES = "ImagesCacheSizeInBytes";

    @Qualifier
    @Retention(RUNTIME)
    public @interface AppInterceptors {
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface NetworkInterceptors {
    }

    // Scopes //

    @Qualifier
    @Retention(RUNTIME)
    public @interface Api {
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface Images {
    }

    // Injection Providers //

    @Provides
    @Singleton
    @Api
    Cache provideHttpCache(@AppModule.ForApplication Context context,
                           @Named(API_CACHE_NAME) String cacheDirName,
                           @Named(API_CACHE_SIZE_IN_BYTES) long cacheSizeMegaBytes) {
        return createCache(context, cacheDirName, cacheSizeMegaBytes);
    }

    @Provides
    @Singleton
    @Images
    Cache provideImagesCache(@AppModule.ForApplication Context context,
                             @Named(IMAGES_CACHE_NAME) String cacheDirName,
                             @Named(IMAGES_CACHE_SIZE_IN_BYTES) long cacheSizeMegaBytes) {
        return createCache(context, cacheDirName, cacheSizeMegaBytes);
    }

    @Provides
    @Singleton
    @Api
    OkHttpClient provideApiOkHttpClient(@Api Cache cache,
                                        @AppInterceptors List<Interceptor> appInterceptor,
                                        @NetworkInterceptors List<Interceptor> networkInterceptor) {
        return createOkHttpClient(cache, appInterceptor, networkInterceptor);
    }

    @Provides
    @Singleton
    @Images
    OkHttpClient provideImagesOkHttpClient(@Images Cache cache,
                                           @AppInterceptors List<Interceptor> appInterceptor,
                                           @NetworkInterceptors List<Interceptor> networkInterceptor) {
        return createOkHttpClient(cache, appInterceptor, networkInterceptor);
    }

    // Static Helpers //

    private static Cache createCache(Context context, String cacheDirName,
                                     long cacheSizeMegaBytes) {
        // Create and set HTTP cache in the application cache directory.
        File cacheDir = new File(context.getCacheDir(), cacheDirName);
        return new Cache(cacheDir, cacheSizeMegaBytes);
    }

    private static OkHttpClient createOkHttpClient(Cache cache,
                                                   List<Interceptor> appInterceptors,
                                                   List<Interceptor> networkInterceptors) {

        OkHttpClient client = new OkHttpClient.Builder()
                .cache(cache)
                .build();

        client.newBuilder().interceptors().addAll(appInterceptors);
        client.newBuilder().networkInterceptors().addAll(networkInterceptors);

        try {
            client.cache().evictAll();
        } catch (IOException ioe) {
            Log.w(TAG, "Error evicting all from cache", ioe);
        }

        return client;
    }

}
