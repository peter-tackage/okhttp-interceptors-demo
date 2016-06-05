package com.moac.android.interceptordemo.rx;

import android.support.annotation.NonNull;

import rx.Scheduler;

public interface ISchedulerProvider {

    @NonNull
    Scheduler ui();

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();
}
