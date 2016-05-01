package com.moac.android.interceptordemo.viewmodel;

import com.moac.android.interceptordemo.api.model.Track;
import com.moac.android.interceptordemo.rx.ElementObserver;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.subjects.PublishSubject;

/**
 * A parameterized view model
 * <p/>
 * Use subscribe to ObservableModel, then fetch
 */
public class TracksViewModelProvider {

    public static final String TAG = TracksViewModelProvider.class.getSimpleName();

    private PublishSubject<List<TrackViewModel>> mModel = PublishSubject.create();
    private PublishSubject<List<Track>> mBridgeModel = PublishSubject.create();

    public TracksViewModelProvider() {
        bindBridge();
    }

    public Observable<TrackViewModel> getObservableViewModel(final long period,
                                                             final TimeUnit timeUnit) {
        return mModel
                .flatMap(trackViewModels -> {
                    Log.i(TAG, "Producing new repeating sequence");
                    return Observable.zip(Observable.from(trackViewModels)
                                                    .filter(trackViewModel -> !TextUtils.isEmpty(
                                                            trackViewModel
                                                                    .getArtworkUrl())),
                                          Observable.interval(period, timeUnit).startWith(0L),
                                          (trackViewModel, __) -> trackViewModel).repeat();
                });
    }

    public Observer<List<Track>> asDestination() {
        return mBridgeModel;
    }

    private void bindBridge() {
        mBridgeModel
                .flatMap(tracks -> {
                    List<TrackViewModel> tvmList = new ArrayList<>();
                    for (Track track : tracks) {
                        tvmList.add(new TrackViewModel(track.getId(),
                                                       track.getTitle(),
                                                       track.getUser().getUsername(),
                                                       convertToHighResUrl(
                                                               track.getArtworkUrl()), 0, ""));
                    }
                    return Observable.just(tvmList);
                }).subscribe(new ElementObserver<>(mModel));
    }

    private static String convertToHighResUrl(String imageUrl) {

        if (TextUtils.isEmpty(imageUrl)) {
            return null;
        }

        final String LARGE_SIZE_SUFFIX = "-large.";
        final String HIGHRES_SIZE_SUFFIX = "-t500x500";

        int sizeSuffixIndex = imageUrl.lastIndexOf(LARGE_SIZE_SUFFIX);
        String fileWithoutSizeSuffix = imageUrl.substring(0, sizeSuffixIndex);
        String extension = imageUrl.substring(imageUrl.lastIndexOf('.'));

        return fileWithoutSizeSuffix + HIGHRES_SIZE_SUFFIX + extension;

    }

}
