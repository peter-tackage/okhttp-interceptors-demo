package com.moac.android.interceptordemo.interceptor;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import java.io.IOException;
import java.util.Random;
import java.util.Set;
import java.util.regex.Pattern;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import rx.Observable;

import static java.util.Collections.emptySet;

public final class ServerErrorDebugInterceptor implements Interceptor {

    @NonNull
    private final IDebugConfigurationProvider mDebugConfigurationProvider;

    @NonNull
    private final Set<String> mUrlRegexSet;

    private final int mErrorCode;

    @NonNull
    private final String mErrorBodyJson;

    @NonNull
    private final Random mRandom;

    public ServerErrorDebugInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider,
            @IntRange(from = 400, to = 499) final int errorCode,
            @NonNull final String errorBodyJson) {
        this(debugConfigurationProvider, emptySet(), errorCode, errorBodyJson);
    }

    public ServerErrorDebugInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider,
            @NonNull final Set<String> urlRegexSet,
            @IntRange(from = 400, to = 499) final int errorCode,
            @NonNull final String errorBodyJson) {
        this(debugConfigurationProvider, urlRegexSet, errorCode, errorBodyJson, new Random());
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        if (shouldError(request)) {
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

    private boolean shouldError(final Request request) {
        return mDebugConfigurationProvider.isServerErrorEnabled()
               && isError()
               && matches(request.url());
    }

    private boolean matches(final HttpUrl url) {
        return mUrlRegexSet.isEmpty() || exists(url);
    }

    private boolean exists(final HttpUrl url) {
        return Observable.from(mUrlRegexSet)
                         .exists(regex -> Pattern.matches(regex, url.toString()))
                         .toBlocking()
                         .single();
    }

    private boolean isError() {
        return mRandom.nextInt(99) <= mDebugConfigurationProvider.getServerErrorPercentage() - 1;
    }

    @VisibleForTesting
    ServerErrorDebugInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider,
            @NonNull final Set<String> urlRegexSet,
            final int errorCode,
            @NonNull final String errorBodyJson,
            @NonNull final Random random) {
        mDebugConfigurationProvider = debugConfigurationProvider;
        mUrlRegexSet = urlRegexSet;
        mErrorCode = errorCode;
        mErrorBodyJson = errorBodyJson;
        mRandom = random;
    }

}
