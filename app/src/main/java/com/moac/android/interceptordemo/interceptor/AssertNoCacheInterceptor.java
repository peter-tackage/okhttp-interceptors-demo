package com.moac.android.interceptordemo.interceptor;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Throws an IllegalStateException if the response contains a Cache-Control header
 */
public class AssertNoCacheInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.header(NeverCacheInterceptor.CACHE_CONTROL_HEADER) != null) {
            throw new IllegalStateException("Expected an empty cache header");
        }
        return response;
    }
}