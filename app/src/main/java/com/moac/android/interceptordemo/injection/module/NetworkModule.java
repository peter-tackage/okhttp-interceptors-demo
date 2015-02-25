package com.moac.android.interceptordemo.injection.module;

import android.content.Context;
import android.util.Log;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Retention;

import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Module(complete = false, library = true)
public class NetworkModule {

    private static final String TAG = NetworkModule.class.getSimpleName();

    // Required configuration settings definitions //

    static final String API_CACHE_NAME = "ApiCacheName";
    static final String API_CACHE_SIZE = "ApiCacheSize";
    static final String IMAGES_CACHE_NAME = "ImagesCacheName";
    static final String IMAGES_CACHE_SIZE = "ImagesCacheSize";

    @Qualifier
    @Retention(RUNTIME)
    public static @interface AppLogging {
    }

    @Qualifier
    @Retention(RUNTIME)
    public static @interface NetworkLogging {
    }

    // Scopes //

    @Qualifier
    @Retention(RUNTIME)
    public static @interface Api {
    }

    @Qualifier
    @Retention(RUNTIME)
    public static @interface Images {
    }

    // Injection Providers //

    @Provides
    @Singleton
    @Api
    Cache provideHttpCache(@AppModule.ForApplication Context context,
                           @Named(API_CACHE_NAME) String cacheDirName,
                           @Named(API_CACHE_SIZE) long cacheSizeMegaBytes) {
        return createCache(context, cacheDirName, cacheSizeMegaBytes);
    }

    @Provides
    @Singleton
    @Images
    Cache provideImagesCache(@AppModule.ForApplication Context context,
                             @Named(IMAGES_CACHE_NAME) String cacheDirName,
                             @Named(IMAGES_CACHE_SIZE) long cacheSizeMegaBytes) {
        return createCache(context, cacheDirName, cacheSizeMegaBytes);
    }

    @Provides
    @Singleton
    StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @Singleton
    @Api
    OkHttpClient provideApiOkHttpClient(@Api Cache cache,
                                        @AppLogging Interceptor appInterceptor,
                                        @NetworkLogging Interceptor networkInterceptor) {
        return createOkHttpClient(cache, appInterceptor, networkInterceptor);
    }

    @Provides
    @Singleton
    @Images
    OkHttpClient provideImagesOkHttpClient(@Images Cache cache,
                                           @AppLogging Interceptor appInterceptor,
                                           @NetworkLogging Interceptor networkInterceptor) {
        return createOkHttpClient(cache, appInterceptor, networkInterceptor);
    }

    // Static Helpers //

    private static Cache createCache(Context context, String cacheDirName, long cacheSizeMegaBytes) {
        try {
            // Create and set HTTP cache in the application cache directory.
            File cacheDir = new File(context.getCacheDir(), cacheDirName);
            return new Cache(cacheDir, cacheSizeMegaBytes);
        } catch (IOException e) {
            Log.e(TAG, "Unable to install disk cache.", e);
            // Don't throw if unable to set cache
            return null;
        }
    }

    private static OkHttpClient createOkHttpClient(Cache cache,
                                                   Interceptor appInterceptor,
                                                   Interceptor networkInterceptor) {
        OkHttpClient client = new OkHttpClient();

        // If failed to create disk cache, still use OkHttp default internal cache
        if (cache != null) {
            client.setCache(cache);
        }

        // Install interceptors
        client.networkInterceptors().add(networkInterceptor);
        client.interceptors().add(appInterceptor);

        return client;
    }

}
