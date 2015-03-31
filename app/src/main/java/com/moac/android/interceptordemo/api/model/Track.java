package com.moac.android.interceptordemo.api.model;

import com.google.gson.annotations.SerializedName;

public class Track {

    @SerializedName("id") private long mId;
    @SerializedName("title") private String mTitle;
    @SerializedName("user") private User mUser;
    @SerializedName("artwork_url") private String mArtworkUrl;

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public User getUser() {
        return mUser;
    }

    public String getArtworkUrl() {
        return mArtworkUrl;
    }
}
