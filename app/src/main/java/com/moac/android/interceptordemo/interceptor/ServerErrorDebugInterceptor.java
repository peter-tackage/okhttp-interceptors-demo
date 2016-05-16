package com.moac.android.interceptordemo.interceptor;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.io.IOException;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public final class ServerErrorDebugInterceptor implements Interceptor {

    @NonNull
    private final Random mRandom;

    @NonNull
    private final IDebugConfigurationProvider mDebugConfigurationProvider;

    private final int mErrorCode;

    @NonNull
    private final String mErrorBodyJson;

    public ServerErrorDebugInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider,
            @IntRange(from = 400, to = 499) final int errorCode,
            @NonNull final String errorBodyJson) {
        this(debugConfigurationProvider, errorCode, errorBodyJson, new Random());
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (shouldError()) {
            return serverError();
        }

        return chain.proceed(request);
    }

    private Response serverError() {
        return new Response.Builder()
                .code(mErrorCode)
                .body(ResponseBody.create(okhttp3.MediaType.parse("application/json"),
                                          mErrorBodyJson))
                .build();
    }

    private boolean shouldError() {
        return mDebugConfigurationProvider.isServerErrorEnabled() && isError();
    }

    private boolean isError() {
        return mRandom.nextInt(99) <= mDebugConfigurationProvider.getServerErrorPercentage() - 1;
    }

    @VisibleForTesting
    ServerErrorDebugInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider,
            final int errorCode,
            @NonNull final String errorBodyJson,
            @NonNull final Random random) {
        mDebugConfigurationProvider = debugConfigurationProvider;
        mErrorCode = errorCode;
        mErrorBodyJson = errorBodyJson;
        mRandom = random;
    }

}
