package com.moac.android.interceptordemo.interceptor;

import com.moac.android.interceptordemo.api.ApiConst;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Add the SoundCloud API client identifier headers to the outgoing request.
 */
public final class SoundCloudRequestInterceptor implements Interceptor {

    private final String mClientId;

    public SoundCloudRequestInterceptor(final String clientId) {
        mClientId = clientId;
    }

    @Override
    public Response intercept(final Chain chain) throws IOException {
        // Add client id to every HTTP request
        Request request = chain.request()
                               .newBuilder()
                               .addHeader(ApiConst.CLIENT_ID_QUERY_PARAM, mClientId)
                               .build();
        return chain.proceed(request);
    }
}

