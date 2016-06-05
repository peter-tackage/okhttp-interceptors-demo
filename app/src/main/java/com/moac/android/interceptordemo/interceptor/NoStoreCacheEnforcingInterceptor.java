package com.moac.android.interceptordemo.interceptor;

import com.moac.android.interceptordemo.config.IDebugConfigurationProvider;

import android.support.annotation.NonNull;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public final class NoStoreCacheEnforcingInterceptor implements Interceptor {

    private static final String CACHE_CONTROL = "Cache-Control";

    private static final String NO_STORE = "no-store";

    @NonNull
    private final IDebugConfigurationProvider mDebugConfigurationProvider;

    public NoStoreCacheEnforcingInterceptor(
            @NonNull final IDebugConfigurationProvider debugConfigurationProvider) {
        mDebugConfigurationProvider = debugConfigurationProvider;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);

        return shouldNoStore(request.url()) ? setNoStoreHeaders(response) : response;
    }

    private static Response setNoStoreHeaders(final Response response) {
        // Can't modify the response CacheControl object, have to manually modify headers.
        return response.newBuilder()
                       .removeHeader(CACHE_CONTROL)
                       .addHeader(CACHE_CONTROL, NO_STORE)
                       .build();
    }

    private boolean shouldNoStore(final HttpUrl url) {
        return isEnabled() && matches(url);
    }

    private boolean isEnabled() {
        return mDebugConfigurationProvider.isNoStoreEnabled();
    }

    private boolean matches(final HttpUrl url) {
        return MatchUtils.matches(url, mDebugConfigurationProvider.getNoStoreUrlRegex());
    }
}
