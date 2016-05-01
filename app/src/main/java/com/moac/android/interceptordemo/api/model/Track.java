package com.moac.android.interceptordemo.api.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;

@AutoValue
public abstract class Track {

    @SerializedName("id")
    public abstract long id();

    @SerializedName("title")
    public abstract String title();

    @SerializedName("user")
    public abstract User user();

    @SerializedName("artwork_url")
    public abstract String artworkUrl();

    @NonNull
    public static TypeAdapter<Track> typeAdapter(@NonNull final Gson gson) {
        return new AutoValue_Track.GsonTypeAdapter(gson);
    }

    @NonNull
    public static Track create(long id, String title, User user, String artworkUrl) {
        return new AutoValue_Track(id, title, user, artworkUrl);
    }
}
