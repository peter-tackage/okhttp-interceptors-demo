package com.moac.android.interceptordemo.interceptor;

import android.support.annotation.IntRange;

public interface IDebugConfigurationProvider {

    //
    // Network Errors
    //

    boolean isNetworkErrorEnabled();

    @IntRange(from = 1, to = 100)
    float getNetworkErrorPercentage();

    //
    // Server Errors
    //

    boolean isServerErrorEnabled();

    @IntRange(from = 1, to = 100)
    float getServerErrorPercentage();

    //
    // Delays
    //

    boolean isNetworkDelayEnabled();

    @IntRange(from = 1, to = 100)
    float getNetworkDelayPercentage();

    @IntRange(from = 0)
    long maxDelayInMillis();

    //
    // Request / Response threshold logging
    //

    long getRequestSizeWarningThresholdBytes();

    long getResponseSizeWarningThresholdBytes();

}
