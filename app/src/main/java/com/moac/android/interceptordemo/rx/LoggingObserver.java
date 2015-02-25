package com.moac.android.interceptordemo.rx;

import android.util.Log;

import rx.Observer;

/*
 * An Observer decorator with logging
 */
public class LoggingObserver<T> implements Observer<T> {

    private final String mLogTag;
    private final Observer<T> mObserver;

    public LoggingObserver(String logTag, Observer<T> observer) {
        mLogTag = logTag;
        mObserver = observer;
    }

    @Override
    public void onCompleted() {
        Log.d(mLogTag, "onCompleted()");
        mObserver.onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        Log.d(mLogTag, "onError()", e);
        mObserver.onError(e);
    }

    @Override
    public void onNext(T t) {
        Log.d(mLogTag, "onNext() - " + t.toString());
        mObserver.onNext(t);
    }
}
