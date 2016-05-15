package com.moac.android.interceptordemo.api;

import android.support.annotation.Nullable;

public interface IArtworkUrlConverter {

    @Nullable
    String convertToHighResUrl(@Nullable final String imageUrl);
}
