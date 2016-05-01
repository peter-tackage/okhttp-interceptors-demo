package com.moac.android.interceptordemo.module;

import android.util.Log;

import com.google.gson.Gson;
import com.moac.android.interceptordemo.api.SoundCloudApi;
import com.moac.android.interceptordemo.api.SoundCloudRequestInterceptor;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

@Module(complete = false, library = true)
public class ApiModule {

    // Required configuration settings definitions //

    static final String API_CLIENT_ID = "ApiClientId";
    static final String API_ENDPOINT_URL = "ApiEndpointUrl";

    // Injection providers //

    @Provides
    @Singleton
    Client provideClient(@NetworkModule.Api OkHttpClient client) {
        return new OkClient(client);
    }

    @Provides
    @Singleton
    RequestInterceptor provideRequestInterceptor(@Named(API_CLIENT_ID) String clientId) {
        return new SoundCloudRequestInterceptor(clientId);
    }

    @Provides
    @Singleton
    Converter provideConverter() {
        return new GsonConverter(new Gson());
    }

    @Provides
    @Singleton
    Endpoint provideEndPoint(@Named(API_ENDPOINT_URL) String endpointUrl) {
        return Endpoints.newFixedEndpoint(endpointUrl);
    }

    @Provides
    @Singleton
    RestAdapter.Log provideLogger() {
        return new RestAdapter.Log() {
            public void log(String msg) {
                Log.i("Retrofit|SoundCloud", msg);
            }
        };
    }

    @Provides
    @Singleton
    RestAdapter provideSoundCloudApiRestAdapter(Client client,
                                                RequestInterceptor interceptor,
                                                Converter converter,
                                                Endpoint endpoint,
                                                RestAdapter.Log logger) {
        return new RestAdapter.Builder()
                .setClient(client)
                .setRequestInterceptor(interceptor)
                .setConverter(converter)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(endpoint)
                .setLog(logger)
                .build();
    }

    @Provides
    @Singleton
    SoundCloudApi provideSoundCloudApi(RestAdapter restAdapter) {
        return restAdapter.create(SoundCloudApi.class);
    }

}
