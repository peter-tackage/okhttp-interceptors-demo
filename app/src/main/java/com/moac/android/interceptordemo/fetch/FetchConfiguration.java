package com.moac.android.interceptordemo.fetch;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class FetchConfiguration {

    @Inject
    public FetchConfiguration() {
    }

    @NonNull
    public Observable<String> getGenreStream() {
        return Observable.just("ambient");
    }

    @NonNull
    public Observable<Long> getLimitStream() {
        return Observable.just(15L); // fetch 15 tracks
    }
}
