package com.moac.android.interceptordemo.viewmodel;

import com.moac.android.interceptordemo.rx.LoggingObserver;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public final class ViewModels {

    public static <T> Subscription fetchInto(Observable<T> source, final Observer<T> destination,
                                             LoggingObserver<T> fetchObserver) {
        return source
                .doOnEach(destination)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(fetchObserver);
    }

}
