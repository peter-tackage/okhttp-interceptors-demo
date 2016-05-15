package com.moac.android.interceptordemo.viewmodel;

import com.google.auto.value.AutoValue;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

@AutoValue
public abstract class Track {

    @NonNull
    public abstract Long getId();

    @NonNull
    public abstract String getTitle();

    @NonNull
    public abstract String getArtist();

    @NonNull
    public abstract String getArtworkUrl();

    @NonNull
    public abstract Long getPlayCount();

    @NonNull
    public abstract String getDuration();

    @NonNull
    public static Track create(long id,
                               @NonNull String title,
                               @NonNull String artist,
                               @NonNull String imageUrl,
                               @IntRange(from = 0) long playCount,
                               @NonNull String duration) {
        return new AutoValue_Track(id, title, artist, imageUrl, playCount, duration);
    }
}
