package com.moac.android.interceptordemo.config;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.Set;

public class DebugConfigurationProvider implements IDebugConfigurationProvider {

    public DebugConfigurationProvider(@NonNull final SharedPreferences sharedPreferences) {
    }

    @Override
    public boolean isNetworkErrorEnabled() {
        return false;
    }

    @Override
    public float getNetworkErrorPercentage() {
        return 0;
    }

    @Override
    public boolean isServerErrorEnabled() {
        return false;
    }

    @Override
    public float getServerErrorPercentage() {
        return 0;
    }

    @Override
    public boolean isNetworkDelayEnabled() {
        return false;
    }

    @Override
    public float getNetworkDelayPercentage() {
        return 0;
    }

    @Override
    public long maxDelayInMillis() {
        return 0;
    }

    @Override
    public long getRequestSizeWarningThresholdBytes() {
        return 0;
    }

    @Override
    public long getResponseSizeWarningThresholdBytes() {
        return 0;
    }

    @Override
    public boolean isNoStoreEnabled() {
        return false;
    }

    @Override
    public Set<String> getNoStoreUrlRegex() {
        return null;
    }
}
