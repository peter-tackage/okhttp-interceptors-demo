package com.moac.android.interceptordemo.fetch;

import com.moac.android.interceptordemo.TrackDataModel;
import com.moac.android.interceptordemo.api.IArtworkUrlConverter;
import com.moac.android.interceptordemo.api.TracksApi;
import com.moac.android.interceptordemo.api.model.ApiTrack;
import com.moac.android.interceptordemo.utils.TextUtils;
import com.moac.android.interceptordemo.viewmodel.Track;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

import static com.moac.android.interceptordemo.rx.RxUtils.safeUnsubscribe;

@Singleton
public class TrackFetcher {

    private static final String TAG = TrackFetcher.class.getSimpleName();

    @NonNull
    private final TracksApi mTracksApi;

    @NonNull
    private final TrackDataModel mTrackDataModel;

    @NonNull
    private final FetchConfiguration mFetchConfiguration;

    @NonNull
    private final IArtworkUrlConverter mArtworkUrlConverter;

    @Nullable
    private Subscription mFetchSubscription;

    @Inject
    public TrackFetcher(@NonNull final TracksApi tracksApi,
                        @NonNull final TrackDataModel trackDataModel,
                        @NonNull final FetchConfiguration fetchConfiguration,
                        @NonNull final IArtworkUrlConverter artworkUrlConverter) {
        mTracksApi = tracksApi;
        mTrackDataModel = trackDataModel;
        mFetchConfiguration = fetchConfiguration;
        mArtworkUrlConverter = artworkUrlConverter;
    }

    public void fetch() {
        mFetchSubscription = Observable.combineLatest(mFetchConfiguration.getGenreStream(),
                                                      mFetchConfiguration.getLimitStream(),
                                                      FetchRequest::create)
                                       .switchMap(
                                               request -> mTracksApi.getTrackList(request.genre(),
                                                                                  request.limit())
                                                                    .toObservable())
                                       .switchMap(Observable::from)
                                       .map(toHighResArtworkUrlApiTrack())
                                       .filter(removeNoArtwork())
                                       .map(toTrack())
                                       .toList()
                                       .filter(removeEmptyList())
                                       .subscribe(mTrackDataModel::set,
                                                  e -> Log.e(TAG, "Error Fetching Tracks", e));
    }

    public void cancelFetch() {
        safeUnsubscribe(mFetchSubscription);
    }

    // TODO Fix up this nullability, use Nullable for the ApiTrack values and use Option<T>
    // for the Track values, filter None

    @NonNull
    private Func1<ApiTrack, ApiTrack> toHighResArtworkUrlApiTrack() {
        return apiTrack -> ApiTrack.copy(apiTrack,
                                         mArtworkUrlConverter
                                                 .convertToHighResUrl(apiTrack.artworkUrl()));
    }

    @NonNull
    private static Func1<ApiTrack, Track> toTrack() {
        return apiTrack -> Track.create(apiTrack.id(),
                                        apiTrack.title(),
                                        apiTrack.user().username(),
                                        apiTrack.artworkUrl(),
                                        0, "");
    }

    @NonNull
    private static Func1<List<Track>, Boolean> removeEmptyList() {
        return tracks -> !tracks.isEmpty();
    }

    @NonNull
    private static Func1<ApiTrack, Boolean> removeNoArtwork() {
        return track -> !TextUtils.isNullOrEmpty(track.artworkUrl());
    }

}
