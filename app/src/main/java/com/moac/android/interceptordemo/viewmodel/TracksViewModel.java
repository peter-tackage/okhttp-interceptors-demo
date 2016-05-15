package com.moac.android.interceptordemo.viewmodel;

import com.moac.android.interceptordemo.TrackDataModel;
import com.moac.android.interceptordemo.rx.ISchedulerProvider;

import android.support.annotation.NonNull;

import java.util.concurrent.TimeUnit;

import rx.Observable;

import static com.moac.android.interceptordemo.rx.Functional.selectLeft;

public final class TracksViewModel {

    @NonNull
    private final TrackDataModel mTrackDataModel;

    @NonNull
    private final ISchedulerProvider mSchedulerProvider;

    public TracksViewModel(@NonNull final TrackDataModel trackDataModel,
                           @NonNull final ISchedulerProvider schedulerProvider) {
        mTrackDataModel = trackDataModel;
        mSchedulerProvider = schedulerProvider;
    }

    @NonNull
    public Observable<Track> getTrackDataStream(final long displayInterval,
                                                @NonNull final TimeUnit timeUnit) {
        return mTrackDataModel.getTracksOnceAndStream()
                              .switchMap(tracks -> Observable.from(tracks).repeat()
                                                             .zipWith(Observable.interval(0,
                                                                                          displayInterval,
                                                                                          timeUnit,
                                                                                          mSchedulerProvider
                                                                                                  .computation()),
                                                                      selectLeft()));
    }

}
