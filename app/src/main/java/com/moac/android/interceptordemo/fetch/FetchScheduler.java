package com.moac.android.interceptordemo.fetch;

import com.moac.android.interceptordemo.rx.ISchedulerProvider;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.subscriptions.SerialSubscription;

import static com.moac.android.interceptordemo.rx.RxUtils.safeUnsubscribe;

public final class FetchScheduler {

    private static final String TAG = FetchScheduler.class.getSimpleName();

    @NonNull
    private final TrackFetcher mTrackFetcher;

    @NonNull
    private final ISchedulerProvider mISchedulerProvider;

    @NonNull
    private SerialSubscription mSubscription = new SerialSubscription();

    public FetchScheduler(@NonNull final TrackFetcher trackFetcher,
                          @NonNull final ISchedulerProvider ISchedulerProvider) {
        mTrackFetcher = trackFetcher;
        mISchedulerProvider = ISchedulerProvider;
    }

    public void start(long interval,
                      @NonNull final TimeUnit timeUnit) {
        mSubscription
                .set(Observable.interval(0, interval, timeUnit, mISchedulerProvider.computation())
                               .subscribe(__ -> mTrackFetcher.fetch(),
                                          e -> Log.e(TAG, "Error scheduling fetch")));
    }

    public void stop() {
        mTrackFetcher.cancelFetch();
        mSubscription = renew();
    }

    @NonNull
    private SerialSubscription renew() {
        safeUnsubscribe(mSubscription);
        return new SerialSubscription();
    }
}
