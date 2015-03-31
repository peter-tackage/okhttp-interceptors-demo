package com.moac.android.interceptordemo.interceptor;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Removes the Cache-Control and Etag control headers
 */
public class NeverCacheInterceptor implements Interceptor {

    protected static final String CACHE_CONTROL_HEADER = "Cache-Control";

    private static final String IF_MODIFIED_SINCE_HEADER = "If-Modified-Since";
    private static final String ETAG_HEADER = "Etag";
    private static final String LAST_MODIFIED_HEADER = "Last-Modified";

    @Override
    public Response intercept(Chain chain) throws IOException {

        // Don't allow Etags and 304 responses either - otherwise we get 304 and empty response body
        // when the content hasn't been modified. This means that we can't preview the JSON in Stetho.
        // Of course you would (probably) NEVER do this in production...
        Request modifiedRequest = chain.request().newBuilder()
                .removeHeader(IF_MODIFIED_SINCE_HEADER).build();

        // Remove any response cache headers to prevent caching
        Response originalResponse = chain.proceed(modifiedRequest);
        return originalResponse.newBuilder()
                .removeHeader(CACHE_CONTROL_HEADER)
                .removeHeader(ETAG_HEADER)
                .removeHeader(LAST_MODIFIED_HEADER)
                .build();

    }
}