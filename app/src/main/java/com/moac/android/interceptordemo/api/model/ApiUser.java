package com.moac.android.interceptordemo.api.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;

@AutoValue
public abstract class ApiUser {

    @SerializedName("id")
    public abstract String id();

    @SerializedName("username")
    public abstract String username();

    @SerializedName("uri")
    public abstract String uri();

    @NonNull
    public static TypeAdapter<ApiUser> typeAdapter(@NonNull final Gson gson) {
        return new AutoValue_ApiUser.GsonTypeAdapter(gson);
    }

    @NonNull
    public static ApiUser create(String id, String username, String uri) {
        return new AutoValue_ApiUser(id, username, uri);
    }
}
