package com.moac.android.interceptordemo.api.model;

import com.google.gson.annotations.SerializedName;

public class Track {

    @SerializedName("id")
    private long mId;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("user")
    private User mUser;
    @SerializedName("artwork_url")
    private String mArtworkUrl;

    public long id() {
        return mId;
    }

    public String title() {
        return mTitle;
    }

    public User user() {
        return mUser;
    }

    public String artworkUrl() {
        return mArtworkUrl;
    }
}
