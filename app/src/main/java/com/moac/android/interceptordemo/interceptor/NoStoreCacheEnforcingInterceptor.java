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
        return mDebugConfigurationProvider.isNoStoreEnabled() &&
               matches(request.url()) ?
                setNoStoreHeaders(response) :
                response;
    }

    private static Response setNoStoreHeaders(final Response response) {
        return response.newBuilder()
                       .removeHeader(CACHE_CONTROL)
                       .addHeader(CACHE_CONTROL, NO_STORE)
                       .build();
    }

    private boolean matches(final HttpUrl url) {
        return mDebugConfigurationProvider.isNoStoreEnabled()
               || exists(url, mDebugConfigurationProvider.getNoStoreUrlRegex());
    }

    private static boolean exists(final HttpUrl url, final Set<String> regexes) {
        return Observable.from(regexes)
                         .exists(regex -> Pattern.matches(regex, url.toString()))
                         .toBlocking()
                         .single();
    }
}
