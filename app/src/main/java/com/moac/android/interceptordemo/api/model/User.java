package com.moac.android.interceptordemo.api.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    protected String mId;
    @SerializedName("username")
    protected String mUsername;
    @SerializedName("uri")
    protected String mUri;

    public String id() {
        return mId;
    }

    public String username() {
        return mUsername;
    }

    public String uri() {
        return mUri;
    }
}
