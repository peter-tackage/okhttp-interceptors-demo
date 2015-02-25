package com.moac.android.interceptordemo.rx;

import android.util.Log;

import rx.Observer;

public class ElementObserver<T> implements Observer<T> {

    private static final String TAG = ElementObserver.class.getSimpleName();

    private final Observer<T> mObserver;

    public ElementObserver(Observer<T> observer) {
        mObserver = observer;
    }
    @Override
    public void onCompleted() {
        Log.d(TAG, "onCompleted()");
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError() - " + e, e);
    }

    @Override
    public void onNext(T t) {
        mObserver.onNext(t);
    }
}
