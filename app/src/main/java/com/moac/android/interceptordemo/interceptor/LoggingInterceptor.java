package com.moac.android.interceptordemo.interceptor;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/*
 * This is only very slightly adapted from OkHttp Recipes version -
 * https://github.com/square/okhttp/tree/master/samples/guide/src/main/java/com/squareup/okhttp/recipes
 */
public class LoggingInterceptor implements Interceptor {
    private final String mLogTag;

    public LoggingInterceptor(String logTag) {
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
        Log.i(mLogTag, String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers()));

        return response;
    }
}