package com.moac.android.interceptordemo.provider;

import com.moac.android.interceptordemo.api.SoundCloudApi;
import com.moac.android.interceptordemo.api.model.Track;

import android.support.annotation.NonNull;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;

@Singleton
public class TracksApi {

    @NonNull
    private final SoundCloudApi mSoundCloudApi;

    @Inject
    public TracksApi(@NonNull final SoundCloudApi soundCloudApi) {
        mSoundCloudApi = soundCloudApi;
    }

    @NonNull
    public Single<List<Track>> getTrackList(@NonNull final String genre, final long limit) {
        return mSoundCloudApi.getTracks(genre, limit);
    }

}
