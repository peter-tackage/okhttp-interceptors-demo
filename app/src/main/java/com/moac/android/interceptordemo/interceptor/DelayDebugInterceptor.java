package com.moac.android.interceptordemo.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class DelayDebugInterceptor implements Interceptor {

    @NonNull
    private final IDebugConfigurationProvider mDebugConfigurationProvider;

    public DelayDebugInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider) {
        mDebugConfigurationProvider = debugConfigurationProvider;
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
        return mDebugConfigurationProvider.isNetworkDelayEnabled();
    }

    private void performDelay() {
        try {
            Random random = new Random();
            long delay = random.nextLong() % mDebugConfigurationProvider.maxDelayInMillis();
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {
            // Do nothing, just proceed.
        }
    }
}