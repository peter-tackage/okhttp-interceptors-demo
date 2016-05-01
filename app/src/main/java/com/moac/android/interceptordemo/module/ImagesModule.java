package com.moac.android.interceptordemo.module;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module(complete = false, library = true)
public class ImagesModule {

    private static final String TAG = ImagesModule.class.getSimpleName();

    @Provides
    @Singleton
    Picasso providePicasso(@AppModule.ForApplication Context context,
                           @NetworkModule.Images OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .loggingEnabled(true)
                .downloader(new OkHttp3Downloader(okHttpClient))
                .build();
    }

}
