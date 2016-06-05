package com.moac.android.interceptordemo.rx;

import rx.Subscription;

public final class RxUtils {

    public static void safeUnsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private RxUtils() {
        throw new AssertionError("No instances.");
    }

}
