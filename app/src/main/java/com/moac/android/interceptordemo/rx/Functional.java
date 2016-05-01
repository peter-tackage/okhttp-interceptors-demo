package com.moac.android.interceptordemo.rx;

import android.support.annotation.NonNull;

import rx.functions.Action1;
import rx.functions.Func2;

public final class Functional {

    @NonNull
    public static <T> Action1<T> ignore1() {
        return __ -> {
        };
    }

    private Functional() {
        throw new AssertionError("No instances.");
    }

    public static <T, U> Func2<T, U, T> takeLeft() {
        return (left, right) -> left;
    }
}
