package com.moac.android.interceptordemo.module;

import com.moac.android.interceptordemo.api.SoundCloudApi;

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

    static final String API_CLIENT_ID = "ApiClientId";
    static final String API_ENDPOINT_URL = "ApiEndpointUrl";

    // Injection providers //

    @Provides
    @Singleton
    OkHttpClient provideClient(@NetworkModule.Api OkHttpClient client) {
        return client;
    }

//    @Provides
//    @Singleton
//    RequestInterceptor provideRequestInterceptor(@Named(API_CLIENT_ID) String clientId) {
//        return new SoundCloudRequestInterceptor(clientId);
//    }

    @Provides
    @Singleton
    GsonConverterFactory provideGsonConverterFactory() {
        return GsonConverterFactory.create();
    }

    @Provides
    @Singleton
    CallAdapter.Factory provideRxJavaFactory() {
        return RxJavaCallAdapterFactory.create();
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
