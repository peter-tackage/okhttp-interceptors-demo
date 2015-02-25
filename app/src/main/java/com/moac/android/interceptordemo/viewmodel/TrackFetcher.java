package com.moac.android.interceptordemo.viewmodel;

import com.moac.android.interceptordemo.api.model.Track;
import com.moac.android.interceptordemo.provider.TracksProvider;
import com.moac.android.interceptordemo.rx.LoggingObserver;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func1;
import rx.functions.Func2;

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

    public TrackFetcher(TracksProvider tracksProvider, Observer<List<Track>> destination, Observer<List<Track>> fetchObserver,
                        Observable<String> genre, Observable<Long> limit, String logTag) {
        this.mTracksProvider = tracksProvider;
        this.mDestination = destination;
        this.mFetchObserver = fetchObserver;
        this.mGenre = genre;
        this.mLimit = limit;
        this.mLogTag = logTag;
    }

    public void fetch() {
        mFetchSubscription = ViewModels.fetchInto(
                Observable.combineLatest(mGenre, mLimit, new Func2<String, Long, RequestData>() {
                    @Override
                    public RequestData call(String genre, Long limit) {
                        return new RequestData(genre, limit);
                    }
                }).flatMap(new Func1<RequestData, Observable<List<Track>>>() {
                    @Override
                    public Observable<List<Track>> call(RequestData request) {
                        return mTracksProvider.getObservable(request.mGenre, request.mLimit);

                    }
                }),
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
            this.mGenre = genre;
            this.mLimit = limit;
        }
    }

}
