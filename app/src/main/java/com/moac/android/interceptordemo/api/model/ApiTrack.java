package com.moac.android.interceptordemo.api.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

@AutoValue
public abstract class ApiTrack {

    @SerializedName("id")
    public abstract long id();

    @SerializedName("title")
    @Nullable
    public abstract String title();

    @SerializedName("user")
    @Nullable
    public abstract ApiUser user();

    @SerializedName("artwork_url")
    @Nullable
    public abstract String artworkUrl();

    @NonNull
    public static TypeAdapter<ApiTrack> typeAdapter(@NonNull final Gson gson) {
        return new AutoValue_ApiTrack.GsonTypeAdapter(gson);
    }

    @NonNull
    public static ApiTrack create(final long id,
                                  @Nullable final String title,
                                  @Nullable final ApiUser apiUser,
                                  @Nullable final String artworkUrl) {
        return new AutoValue_ApiTrack(id, title, apiUser, artworkUrl);
    }

    @NonNull
    public static ApiTrack copy(@NonNull ApiTrack apiTrack,
                                @Nullable final String artworkUrl) {
        return new AutoValue_ApiTrack(apiTrack.id(),
                                      apiTrack.artworkUrl(),
                                      apiTrack.user(),
                                      artworkUrl);
    }
}
