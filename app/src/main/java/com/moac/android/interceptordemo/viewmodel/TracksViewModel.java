package com.moac.android.interceptordemo.viewmodel;

import com.moac.android.interceptordemo.TrackDataModel;
import com.moac.android.interceptordemo.api.model.Track;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

import static com.moac.android.interceptordemo.rx.Functional.selectLeft;

public final class TracksViewModel {

    @NonNull
    private final TrackDataModel mTrackDataModel;

    public TracksViewModel(@NonNull final TrackDataModel trackDataModel) {
        mTrackDataModel = trackDataModel;
    }

    @NonNull
    public Observable<TrackData> getTrackDataStream(final long displayInterval,
                                                    @NonNull final TimeUnit timeUnit) {
        return mTrackDataModel.getTracksOnceAndStream()
                              .switchMap(tracks -> Observable.from(tracks)
                                                             .filter(removeNoArtwork())
                                                             .map(this::toTrackData))
                              .toList()
                              .switchMap(tracks -> Observable.from(tracks)
                                                             .zipWith(Observable.interval(0,
                                                                                          displayInterval,
                                                                                          timeUnit),
                                                                      selectLeft())
                                                             .repeat());
    }

    @NonNull
    private Func1<Track, Boolean> removeNoArtwork() {
        return track -> !TextUtils.isEmpty(track.artworkUrl());
    }

    @NonNull
    private TrackData toTrackData(@NonNull final Track track) {
        return TrackData.create(track.id(),
                                track.title(),
                                track.user().username(),
                                convertToHighResUrl(track.artworkUrl()), 0, "");
    }

    @NonNull
    private static String convertToHighResUrl(@NonNull final String imageUrl) {

        if (TextUtils.isEmpty(imageUrl)) {
            throw new IllegalArgumentException("Image Url cannot be null or empty");
        }

        final String LARGE_SIZE_SUFFIX = "-large.";
        final String HIGHRES_SIZE_SUFFIX = "-t500x500";

        int sizeSuffixIndex = imageUrl.lastIndexOf(LARGE_SIZE_SUFFIX);
        String fileWithoutSizeSuffix = imageUrl.substring(0, sizeSuffixIndex);
        String extension = imageUrl.substring(imageUrl.lastIndexOf('.'));

        return fileWithoutSizeSuffix + HIGHRES_SIZE_SUFFIX + extension;
    }

}