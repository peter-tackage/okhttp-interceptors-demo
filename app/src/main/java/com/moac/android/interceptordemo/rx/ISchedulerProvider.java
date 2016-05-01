package com.moac.android.interceptordemo.rx;

import android.support.annotation.NonNull;

import rx.Scheduler;

/**
 * @author Peter Tackage
 * @since 01/05/16
 */
public interface ISchedulerProvider {

    @NonNull
    Scheduler ui();

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();
}
