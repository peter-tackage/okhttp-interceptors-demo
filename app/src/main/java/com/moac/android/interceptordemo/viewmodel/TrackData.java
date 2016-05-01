package com.moac.android.interceptordemo.viewmodel;

import com.google.auto.value.AutoValue;

import android.support.annotation.NonNull;

@AutoValue
public abstract class TrackData {

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
    public static TrackData create(long id,
                                   @NonNull String title,
                                   @NonNull String artist,
                                   @NonNull String imageUrl,
                                   long playCount,
                                   @NonNull String duration) {
        return new AutoValue_TrackData(id, title, artist, imageUrl, playCount, duration);
    }
}
