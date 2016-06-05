package com.moac.android.interceptordemo.config;

import android.support.annotation.IntRange;

import java.util.Set;

public interface IDebugConfigurationProvider {

    //
    // Network Errors
    //

    boolean isNetworkErrorEnabled();

    @IntRange(from = 0, to = 100)
    float getNetworkErrorPercentage();

    //
    // Server Errors
    //

    boolean isServerErrorEnabled();

    @IntRange(from = 0, to = 100)
    float getServerErrorPercentage();

    //
    // Delays
    //

    boolean isNetworkDelayEnabled();

    @IntRange(from = 0, to = 100)
    float getNetworkDelayPercentage();

    @IntRange(from = 0)
    long maxDelayInMillis();

    //
    // Request / Response threshold logging
    //

    long getRequestSizeWarningThresholdBytes();

    long getResponseSizeWarningThresholdBytes();

    //
    // No Store
    //

    boolean isNoStoreEnabled();

    Set<String> getNoStoreUrlRegex();
}
