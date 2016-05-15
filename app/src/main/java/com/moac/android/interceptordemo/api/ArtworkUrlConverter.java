package com.moac.android.interceptordemo.api;

import com.moac.android.interceptordemo.utils.TextUtils;

import android.support.annotation.Nullable;

public final class ArtworkUrlConverter implements IArtworkUrlConverter {

    private static final String LARGE_SIZE_SUFFIX = "-large.";
    private static final String HIGHRES_SIZE_SUFFIX = "-t500x500";

    @Override
    @Nullable
    public String convertToHighResUrl(@Nullable final String imageUrl) {

        if (TextUtils.isNullOrEmpty(imageUrl)) {
            return null;
        }

        final int sizeSuffixIndex = imageUrl.lastIndexOf(LARGE_SIZE_SUFFIX);

        if (sizeSuffixIndex == -1) {
            return null;
        }

        String fileWithoutSizeSuffix = imageUrl.substring(0, sizeSuffixIndex);
        String extension = imageUrl.substring(imageUrl.lastIndexOf('.'));

        return fileWithoutSizeSuffix + HIGHRES_SIZE_SUFFIX + extension;
    }
}
