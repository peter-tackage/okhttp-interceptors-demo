package com.moac.android.interceptordemo.api;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class SoundCloudRequestInterceptor implements Interceptor {

    private final String mClientId;

    public SoundCloudRequestInterceptor(final String clientId) {
        mClientId = clientId;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        // Add client id to request
        Request request = chain.request()
                               .newBuilder()
                               .addHeader(ApiConst.CLIENT_ID_QUERY_PARAM, mClientId)
                               .build();
        return chain.proceed(request);
    }
}

