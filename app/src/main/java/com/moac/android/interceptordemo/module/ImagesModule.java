package com.moac.android.interceptordemo.module;

import android.content.Context;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(complete = false, library = true)
public class ImagesModule {

    private static final String TAG = ImagesModule.class.getSimpleName();

    @Provides
    @Singleton
    Picasso providePicasso(@AppModule.ForApplication Context context,
                           @NetworkModule.Images OkHttpClient okHttpClient) {
        return new Picasso.Builder(context)
                .loggingEnabled(true)
                .downloader(new OkHttpDownloader(okHttpClient))
                .build();
    }

}
