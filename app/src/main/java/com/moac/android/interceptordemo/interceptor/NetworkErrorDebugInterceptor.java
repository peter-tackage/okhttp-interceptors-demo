package com.moac.android.interceptordemo.interceptor;

import com.moac.android.interceptordemo.config.IDebugConfigurationProvider;

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
public final class NetworkErrorDebugInterceptor implements Interceptor {

    @NonNull
    private final IDebugConfigurationProvider mDebugConfigurationProvider;

    @NonNull
    private final Random mRandom;

    public NetworkErrorDebugInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider) {
        this(debugConfigurationProvider, new Random());
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        // Before the actual network call, so the call doesn't actually succeed!
        if (shouldError()) {
            return throwNetworkException();
        }

        return chain.proceed(request);
    }

    private boolean shouldError() {
        return isEnabled() && isError();
    }

    private boolean isEnabled() {
        return mDebugConfigurationProvider.isNetworkErrorEnabled();
    }

    private boolean isError() {
        return mRandom.nextInt(99) <= mDebugConfigurationProvider.getNetworkErrorPercentage() - 1;
    }

    private Response throwNetworkException() throws IOException {
        throw new IOException("You are lucky recipient of a network error!");
    }

    @VisibleForTesting
    NetworkErrorDebugInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider,
            @NonNull final Random random) {
        mDebugConfigurationProvider = debugConfigurationProvider;
        mRandom = random;
    }

}