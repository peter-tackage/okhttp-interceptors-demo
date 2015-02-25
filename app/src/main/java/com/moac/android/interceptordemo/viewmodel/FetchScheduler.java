package com.moac.android.interceptordemo.viewmodel;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

import static com.moac.android.interceptordemo.rx.RxUtils.safeUnsubscribe;

public class FetchScheduler {

    private Subscription mSubscription;

    public FetchScheduler() {
    }

    public void start(long period, TimeUnit timeUnit, final TrackFetcher trackFetcher) {
        safeUnsubscribe(mSubscription);
        mSubscription = Observable.timer(0, period, timeUnit)
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long count) {
                        trackFetcher.fetch();
                    }
                });
    }

    public void stop() {
        safeUnsubscribe(mSubscription);
    }
}
