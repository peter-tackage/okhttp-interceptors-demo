package com.moac.android.interceptordemo.api.model;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import android.support.annotation.NonNull;

@AutoValue
public abstract class User {

    @SerializedName("id")
    public abstract String id();

    @SerializedName("username")
    public abstract String username();

    @SerializedName("uri")
    public abstract String uri();

    @NonNull
    public static TypeAdapter<User> typeAdapter(@NonNull final Gson gson) {
        return new AutoValue_User.GsonTypeAdapter(gson);
    }

    @NonNull
    public static User create(String id, String username, String uri) {
        return new AutoValue_User(id, username, uri);
    }
}
