package com.moac.android.interceptordemo.viewmodel;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;

import static com.moac.android.interceptordemo.rx.RxUtils.safeUnsubscribe;

public class FetchScheduler {

    private Subscription mSubscription;

    public void start(long period, TimeUnit timeUnit, final TrackFetcher trackFetcher) {
        safeUnsubscribe(mSubscription);
        mSubscription = Observable.interval(0, period, timeUnit)
                                  .subscribe(__ -> trackFetcher.fetch());
    }

    public void stop() {
        safeUnsubscribe(mSubscription);
    }
}
