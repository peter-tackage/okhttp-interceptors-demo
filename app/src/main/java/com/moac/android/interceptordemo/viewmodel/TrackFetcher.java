package com.moac.android.interceptordemo.viewmodel;

import com.moac.android.interceptordemo.api.model.Track;
import com.moac.android.interceptordemo.provider.TracksProvider;
import com.moac.android.interceptordemo.rx.LoggingObserver;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

import static com.moac.android.interceptordemo.rx.RxUtils.safeUnsubscribe;

/*
 * Transactional Fetcher
 */
public class TrackFetcher {

    @NonNull
    private final TracksProvider mTracksProvider;
    @NonNull
    private final Observer<List<Track>> mDestination;
    @NonNull
    private final Observer<List<Track>> mFetchObserver;
    @NonNull
    private final Observable<String> mGenre;
    @NonNull
    private final Observable<Long> mLimit;
    @NonNull
    private final String mLogTag;

    @Nullable
    private Subscription mFetchSubscription;

    public TrackFetcher(@NonNull final TracksProvider tracksProvider,
                        @NonNull final Observer<List<Track>> destination,
                        @NonNull final Observer<List<Track>> fetchObserver,
                        @NonNull final Observable<String> genre,
                        @NonNull final Observable<Long> limit,
                        @NonNull final String logTag) {
        mTracksProvider = tracksProvider;
        mDestination = destination;
        mFetchObserver = fetchObserver;
        mGenre = genre;
        mLimit = limit;
        mLogTag = logTag;
    }

    public void fetch() {
        mFetchSubscription = ViewModels.fetchInto(
                Observable.combineLatest(mGenre, mLimit, FetchRequest::create)
                          .flatMap(request -> mTracksProvider
                                  .getTrackList(request.genre(), request.limit())
                                  .toObservable()),
                mDestination,
                new LoggingObserver<>(mLogTag, mFetchObserver));
    }

    public void cancelFetch() {
        safeUnsubscribe(mFetchSubscription);
    }

}
