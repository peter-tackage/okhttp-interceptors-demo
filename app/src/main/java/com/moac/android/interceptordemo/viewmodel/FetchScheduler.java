package com.moac.android.interceptordemo.viewmodel;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subscriptions.SerialSubscription;

import static com.moac.android.interceptordemo.rx.RxUtils.safeUnsubscribe;

public class FetchScheduler {

    private static final String TAG = FetchScheduler.class.getSimpleName();

    @NonNull
    private final TrackFetcher mTrackFetcher;

    @NonNull
    private SerialSubscription mSubscription = new SerialSubscription();

    public FetchScheduler(@NonNull final TrackFetcher trackFetcher) {
        mTrackFetcher = trackFetcher;
    }

    public void start(long interval,
                      @NonNull final TimeUnit timeUnit) {
        mSubscription.set(Observable.interval(0, interval, timeUnit)
                                    .subscribe(__ -> mTrackFetcher.fetch(),
                                               e -> Log.e(TAG, "Error scheduling fetch")));
    }

    public void stop() {
        mTrackFetcher.cancelFetch();
        safeUnsubscribe(mSubscription);
    }
}
