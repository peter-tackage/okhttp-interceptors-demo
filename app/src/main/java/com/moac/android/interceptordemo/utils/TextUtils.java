package com.moac.android.interceptordemo.utils;

import android.support.annotation.Nullable;

public final class TextUtils {

    public static boolean isNullOrEmpty(@Nullable String string) {
        return string == null || string.isEmpty();
    }

    private TextUtils() {
        throw new AssertionError("No instances.");
    }
}
