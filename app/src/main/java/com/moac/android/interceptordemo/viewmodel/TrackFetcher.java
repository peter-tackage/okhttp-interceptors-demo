package com.moac.android.interceptordemo.viewmodel;

import com.moac.android.interceptordemo.api.model.Track;
import com.moac.android.interceptordemo.provider.TracksProvider;
import com.moac.android.interceptordemo.rx.LoggingObserver;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;

import static com.moac.android.interceptordemo.rx.RxUtils.safeUnsubscribe;

/*
 * Transactional Fetcher
 */
public class TrackFetcher {

    private final TracksProvider mTracksProvider;
    private final Observer<List<Track>> mDestination;
    private final Observer<List<Track>> mFetchObserver;
    private final String mLogTag;

    // Query fields
    private final Observable<String> mGenre;
    private final Observable<Long> mLimit;

    private Subscription mFetchSubscription;

    public TrackFetcher(TracksProvider tracksProvider, Observer<List<Track>> destination,
                        Observer<List<Track>> fetchObserver,
                        Observable<String> genre, Observable<Long> limit, String logTag) {
        mTracksProvider = tracksProvider;
        mDestination = destination;
        mFetchObserver = fetchObserver;
        mGenre = genre;
        mLimit = limit;
        mLogTag = logTag;
    }

    public void fetch() {
        mFetchSubscription = ViewModels.fetchInto(
                Observable.combineLatest(mGenre, mLimit, RequestData::new)
                          .flatMap(request -> mTracksProvider
                                  .getObservable(request.mGenre, request.mLimit)),
                mDestination,
                new LoggingObserver<>(mLogTag, mFetchObserver));
    }

    public void cancelFetch() {
        safeUnsubscribe(mFetchSubscription);
    }

    private static class RequestData {

        String mGenre;
        Long mLimit;

        private RequestData(String genre, Long limit) {
            mGenre = genre;
            mLimit = limit;
        }
    }

}
