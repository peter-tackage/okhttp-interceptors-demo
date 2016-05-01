package com.moac.android.interceptordemo.rx;

import rx.functions.Func2;

public final class Functional {

    public static <T, U> Func2<T, U, T> selectLeft() {
        return (left, right) -> left;
    }

    private Functional() {
        throw new AssertionError("No instances.");
    }
}
