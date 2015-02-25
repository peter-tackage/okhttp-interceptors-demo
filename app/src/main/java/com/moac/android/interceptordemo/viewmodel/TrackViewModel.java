package com.moac.android.interceptordemo.viewmodel;

public class TrackViewModel {
    private final long mId;
    private final String mTitle;
    private final String mArtist;
    private final String mArtworkUrl;
    private final long mPlayCount;
    private final String mDuration;

    public TrackViewModel(long id, String title, String artist, String imageUrl, long playCount, String duration) {
        this.mId = id;
        this.mTitle = title;
        this.mArtist = artist;
        this.mArtworkUrl = imageUrl;
        this.mPlayCount = playCount;
        this.mDuration = duration;
    }

    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public String getArtworkUrl() {
        return mArtworkUrl;
    }

    public long getPlayCount() {
        return mPlayCount;
    }

    public String getDuration() {
        return mDuration;
    }

    @Override
    public String toString() {
        return "TrackViewModel{" +
                "mId=" + mId +
                ", mTitle='" + mTitle + '\'' +
                ", mArtist='" + mArtist + '\'' +
                ", mArtworkUrl='" + mArtworkUrl +
                '}';
    }
}
