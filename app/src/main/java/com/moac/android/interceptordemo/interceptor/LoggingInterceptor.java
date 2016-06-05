package com.moac.android.interceptordemo.interceptor;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/*
 * Logs the request and the response.
 *
 * This is only very slightly adapted from OkHttp Recipes version -
 * https://github.com/square/okhttp/tree/master/samples/guide/src/main/java/com/squareup/okhttp/recipes
 */
public final class LoggingInterceptor implements Interceptor {

    @NonNull
    private final String mLogTag;

    public LoggingInterceptor(@NonNull final String logTag) {
        mLogTag = logTag;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Log.i(mLogTag, String.format("Sending request %s on %s%n%s",
                                     request.url(), chain.connection(), request.headers()));

        Response response = chain.proceed(request);

        long t2 = System.nanoTime();
        Log.i(mLogTag, String.format("Received response for %s in %.1fms%n%s. Cached: %s",
                                     response.request().url(), (t2 - t1) / 1e6d,
                                     response.headers(),
                                     isCached(response)));

        return response;
    }

    private static boolean isCached(final Response response) {
        return response.networkResponse() == null;
    }
}