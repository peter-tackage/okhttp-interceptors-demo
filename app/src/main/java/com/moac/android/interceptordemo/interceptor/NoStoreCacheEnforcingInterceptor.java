package com.moac.android.interceptordemo.interceptor;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.Set;
import java.util.regex.Pattern;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;

import static java.util.Collections.emptySet;

public final class NoStoreCacheEnforcingInterceptor implements Interceptor {

    private static final String CACHE_CONTROL = "Cache-Control";

    @NonNull
    private final IDebugConfigurationProvider mDebugConfigurationProvider;

    @NonNull
    private final Set<String> mUrlRegexSet;

    public NoStoreCacheEnforcingInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider) {
        this(debugConfigurationProvider, emptySet());
    }

    public NoStoreCacheEnforcingInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider,
            @NonNull final Set<String> urlRegexSet) {
        mDebugConfigurationProvider = debugConfigurationProvider;
        mUrlRegexSet = urlRegexSet;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);
        return matches(request.url()) ?
                setNoStoreHeaders(response) :
                response;
    }

    private static Response setNoStoreHeaders(final Response response) {
        return response.newBuilder()
                       .removeHeader(CACHE_CONTROL)
                       .addHeader(CACHE_CONTROL, "no-store")
                       .build();
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
}
