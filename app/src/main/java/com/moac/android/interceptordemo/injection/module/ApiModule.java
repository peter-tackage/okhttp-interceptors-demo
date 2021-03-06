package com.moac.android.interceptordemo.injection.module;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapterFactory;

import com.moac.android.interceptordemo.api.SoundCloudApi;
import com.moac.android.interceptordemo.api.model.AutoValueGsonTypeAdapterFactory;
import com.moac.android.interceptordemo.rx.ISchedulerProvider;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(complete = false, library = true)
public class ApiModule {

    // Required configuration settings definitions //

    static final String API_ENDPOINT_URL = "ApiEndpointUrl";

    // Injection providers //

    @Provides
    @Singleton
    OkHttpClient provideClient(@NetworkModule.Api OkHttpClient client) {
        return client;
    }

    @Provides
    @Singleton
    TypeAdapterFactory provideAutoValueGsonTypeAdapterFactory() {
        return new AutoValueGsonTypeAdapterFactory();
    }

    @Provides
    @Singleton
    Gson provideGson(TypeAdapterFactory typeAdapterFactory) {
        return new GsonBuilder().registerTypeAdapterFactory(typeAdapterFactory).create();
    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverterFactory(Gson gson) {
        return GsonConverterFactory.create(gson);
    }

    @Provides
    @Singleton
    CallAdapter.Factory provideRxJavaFactory(ISchedulerProvider schedulerProvider) {
        return RxJavaCallAdapterFactory.createWithScheduler(schedulerProvider.io());
    }

    @Provides
    @Singleton
    Retrofit provideSoundCloudApiRestAdapter(OkHttpClient client,
                                             @Named(API_ENDPOINT_URL) String baseUrl,
                                             CallAdapter.Factory callAdapterFactory,
                                             GsonConverterFactory converterFactory) {

        return new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converterFactory)
                .build();

    }

    @Provides
    @Singleton
    SoundCloudApi provideSoundCloudApi(Retrofit retrofit) {
        return retrofit.create(SoundCloudApi.class);
    }

}
