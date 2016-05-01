package com.moac.android.interceptordemo.test;

import com.moac.android.interceptordemo.rx.ISchedulerProvider;

import android.support.annotation.NonNull;

import rx.Scheduler;
import rx.schedulers.TestScheduler;

/**
 * @author Peter Tackage
 * @since 01/05/16
 */
public class TestSchedulerProvider implements ISchedulerProvider {

    @NonNull
    private final TestScheduler mTestScheduler;

    public TestSchedulerProvider(@NonNull final TestScheduler testScheduler) {
        mTestScheduler = testScheduler;
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return mTestScheduler;
    }

    @NonNull
    @Override
    public Scheduler computation() {
        return mTestScheduler;
    }

    @NonNull
    @Override
    public Scheduler io() {
        return mTestScheduler;
    }
}
