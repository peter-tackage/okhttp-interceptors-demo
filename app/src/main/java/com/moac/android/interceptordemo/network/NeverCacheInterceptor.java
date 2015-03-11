package com.moac.android.interceptordemo.network;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class NeverCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        // Remove any response cache header to prevent caching
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .removeHeader("Cache-Control")
                .build();

    }
}