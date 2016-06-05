package com.moac.android.interceptordemo.interceptor;

import com.moac.android.interceptordemo.config.IDebugConfigurationProvider;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.io.IOException;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class DelayDebugInterceptor implements Interceptor {

    @NonNull
    private final IDebugConfigurationProvider mDebugConfigurationProvider;

    @NonNull
    private final Random mRandom;

    public DelayDebugInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider) {
        this(debugConfigurationProvider, new Random());
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (shouldDelay()) {
            performDelay();
        }

        return chain.proceed(request).networkResponse();
    }

    private boolean shouldDelay() {
        return isEnabled() && isDelay();
    }

    private boolean isEnabled() {
        return mDebugConfigurationProvider.isNetworkDelayEnabled();
    }

    private boolean isDelay() {
        return mRandom.nextInt(99) <= mDebugConfigurationProvider.getNetworkDelayPercentage() - 1;
    }

    private void performDelay() {
        try {
            long delay = mRandom.nextLong() % mDebugConfigurationProvider.maxDelayInMillis();
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
            // Do nothing, just proceed.
        }
    }

    @VisibleForTesting
    DelayDebugInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider,
            @NonNull final Random random) {
        mDebugConfigurationProvider = debugConfigurationProvider;
        mRandom = random;
    }
}