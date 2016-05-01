package com.moac.android.interceptordemo.viewmodel;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

import static com.moac.android.interceptordemo.rx.RxUtils.safeUnsubscribe;

public class FetchScheduler {

    private static final String TAG = FetchScheduler.class.getSimpleName();

    @Nullable
    private Subscription mSubscription;

    public void start(long period,
                      @NonNull final TimeUnit timeUnit,
                      @NonNull final TrackFetcher trackFetcher) {
        safeUnsubscribe(mSubscription);
        mSubscription = Observable.interval(0, period, timeUnit)
                                  .subscribe(__ -> trackFetcher.fetch(),
                                             e -> Log.e(TAG, "Error scheduling fetch"));
    }

    public void stop() {
        safeUnsubscribe(mSubscription);
    }
}
