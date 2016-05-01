package com.moac.android.interceptordemo.fetch;

import com.moac.android.interceptordemo.TrackDataModel;
import com.moac.android.interceptordemo.provider.TracksApi;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.Subscription;

import static com.moac.android.interceptordemo.rx.RxUtils.safeUnsubscribe;

/*
 * Transactional Fetcher
 */
@Singleton
public class TrackFetcher {

    private static final String TAG = TrackFetcher.class.getSimpleName();

    @NonNull
    private final TracksApi mTracksApi;

    @NonNull
    private final TrackDataModel mTrackDataModel;

    @NonNull
    private final FetchConfiguration mFetchConfiguration;

    @Nullable
    private Subscription mFetchSubscription;

    @Inject
    public TrackFetcher(@NonNull final TracksApi tracksApi,
                        @NonNull final TrackDataModel trackDataModel,
                        @NonNull final FetchConfiguration fetchConfiguration) {
        mTracksApi = tracksApi;
        mTrackDataModel = trackDataModel;
        mFetchConfiguration = fetchConfiguration;
    }

    public void fetch() {
        mFetchSubscription = Observable.combineLatest(mFetchConfiguration.getGenre(),
                                                      mFetchConfiguration.getLimit(),
                                                      FetchRequest::create)
                                       .switchMap(
                                               request -> mTracksApi.getTrackList(request.genre(),
                                                                                  request.limit())
                                                                    .toObservable())
                                       .subscribe(mTrackDataModel::set,
                                                  e -> Log.e(TAG, "Error Fetching Tracks", e));
    }

    public void cancelFetch() {
        safeUnsubscribe(mFetchSubscription);
    }
}
