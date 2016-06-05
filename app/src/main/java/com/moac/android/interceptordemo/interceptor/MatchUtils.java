package com.moac.android.interceptordemo.interceptor;

import android.support.annotation.NonNull;

import java.util.Set;
import java.util.regex.Pattern;

import okhttp3.HttpUrl;
import rx.Observable;

public final class MatchUtils {

    public static boolean matches(@NonNull final HttpUrl url, @NonNull final Set<String> regexSet) {
        return regexSet.isEmpty()
               || Observable.from(regexSet)
                            .exists(regex -> Pattern.matches(regex, url.toString()))
                            .toBlocking()
                            .single();
    }

    private MatchUtils() {
        throw new AssertionError("No instances.");
    }

}
