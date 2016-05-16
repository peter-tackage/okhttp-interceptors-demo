package com.moac.android.interceptordemo.interceptor;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/*
 * Logs a warning when the request or the response body exceeds an allowed threshold.
 *
 * Note: <i>This doesn't work for request/response types that don't provide a non-negative
 * Content Length value, such as Chunked-Transfer-Encoding.</i>
 */
public final class LargeBodyWarningLoggingInterceptor implements Interceptor {

    @NonNull
    private final String mLogTag;

    @NonNull
    private final IDebugConfigurationProvider mDebugConfigurationProvider;

    public LargeBodyWarningLoggingInterceptor(@NonNull final String logTag,
                                              @NonNull final IDebugConfigurationProvider debugConfigurationProvider) {
        mLogTag = logTag;
        mDebugConfigurationProvider = debugConfigurationProvider;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (requestBodySizeExceedsThreshold(request)) {
            logWarningExceedsRequestBodyThreshold(request);
        }

        Response response = chain.proceed(request);

        if (responseBodySizeExceedsThreshold(response)) {
            logWarningExceedsResponseBodyThreshold(response);
        }

        return response;
    }

    private boolean responseBodySizeExceedsThreshold(final Response response) {
        return response.body().contentLength() > mDebugConfigurationProvider
                .getResponseSizeWarningThresholdBytes();
    }

    private boolean requestBodySizeExceedsThreshold(final Request request) throws IOException {
        return request.body().contentLength() > mDebugConfigurationProvider
                .getRequestSizeWarningThresholdBytes();
    }

    private void logWarningExceedsRequestBodyThreshold(final Request request) throws IOException {
        Log.w(mLogTag, String.format("Request body for %s, size: %d exceeds %d bytes",
                                     request.url(),
                                     request.body().contentLength(),
                                     mDebugConfigurationProvider
                                             .getResponseSizeWarningThresholdBytes()));
    }

    private void logWarningExceedsResponseBodyThreshold(final Response response) {
        Log.w(mLogTag, String.format("Received response body for %s, size: %d exceeds %d bytes",
                                     response.request().url(),
                                     response.body().contentLength(),
                                     mDebugConfigurationProvider
                                             .getResponseSizeWarningThresholdBytes()));
    }
}