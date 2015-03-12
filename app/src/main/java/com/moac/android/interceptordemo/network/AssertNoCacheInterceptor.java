package com.moac.android.interceptordemo.network;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class AssertNoCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (originalResponse.header("Cache-Control") != null) {
            throw new IllegalStateException("Expected an empty cache header");
        }
        return originalResponse;
    }
}