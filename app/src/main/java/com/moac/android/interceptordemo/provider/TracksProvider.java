package com.moac.android.interceptordemo.provider;

import com.moac.android.interceptordemo.api.SoundCloudApi;
import com.moac.android.interceptordemo.api.model.Track;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class TracksProvider {

    private final SoundCloudApi mSoundCloudApi;

    @Inject
    public TracksProvider(SoundCloudApi soundCloudApi) {
        mSoundCloudApi = soundCloudApi;
    }

    public Observable<List<Track>> getObservable(final String genre, final long limit) {
        return mSoundCloudApi.getTracks(genre, limit);
    }

}
