package com.moac.android.interceptordemo.interceptor;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.io.IOException;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This should be ordered last in the Interceptor chain to ensure that other pre-call
 * interceptors are invoked in the event that this throws an IOException.
 */
public final class AuthorizationErrorDebugInterceptor implements Interceptor {

    @NonNull
    private final Random mRandom;

    @NonNull
    private final IDebugConfigurationProvider mDebugConfigurationProvider;

    public AuthorizationErrorDebugInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider) {
        this(debugConfigurationProvider, new Random());
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (shouldError()) {
            throw new IOException("You are lucky recipient of a network error!");
        }

        return chain.proceed(request);
    }

    private boolean shouldError() {
        return mDebugConfigurationProvider.isNetworkErrorEnabled()
               && isError();
    }

    private boolean isError() {
        return mRandom.nextInt(99) <= mDebugConfigurationProvider.getNetworkDelayPercentage() - 1;
    }

    @VisibleForTesting
    AuthorizationErrorDebugInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider,
            @NonNull final Random random) {
        mDebugConfigurationProvider = debugConfigurationProvider;
        mRandom = random;
    }

}