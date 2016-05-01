package com.moac.android.interceptordemo.fetch;

import com.google.auto.value.AutoValue;

import android.support.annotation.NonNull;

@AutoValue
abstract class FetchRequest {

    @NonNull
    abstract String genre();

    @NonNull
    abstract Long limit();

    @NonNull
    static FetchRequest create(@NonNull final String genre,
                               @NonNull final Long limit) {
        return new AutoValue_FetchRequest(genre, limit);
    }
}
