package com.moac.android.interceptordemo;

import com.moac.android.interceptordemo.viewmodel.Track;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class TrackDataModel {

    @Inject
    public TrackDataModel() {
    }

    private Observable<List<Track>> mTracks;

    public void set(@NonNull final List<Track> tracks) {

    }

    public Observable<List<Track>> getTracksOnceAndStream() {
        return mTracks;
    }

}
