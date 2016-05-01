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
import rx.functions.Func1;
import rx.functions.Func2;
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
                .flatMap(new Func1<List<TrackViewModel>, Observable<TrackViewModel>>() {
                    @Override
                    public Observable<TrackViewModel> call(List<TrackViewModel> trackViewModels) {
                        Log.i(TAG, "Producing new repeating sequence");
                        return Observable.zip(Observable.from(trackViewModels)
                                                        .filter(new Func1<TrackViewModel, Boolean>() {
                                                            @Override
                                                            public Boolean call(
                                                                    TrackViewModel trackViewModel) {
                                                                return !TextUtils.isEmpty(
                                                                        trackViewModel
                                                                                .getArtworkUrl());
                                                            }
                                                        }),
                                              Observable.interval(period, timeUnit).startWith(0l),
                                              new Func2<TrackViewModel, Long, TrackViewModel>() {
                                                  @Override
                                                  public TrackViewModel call(
                                                          TrackViewModel trackViewModel,
                                                          Long aLong) {
                                                      return trackViewModel;
                                                  }
                                              }).repeat();
                    }
                });
    }

    public Observer<List<Track>> asDestination() {
        return mBridgeModel;
    }

    private void bindBridge() {
        mBridgeModel
                .flatMap(new Func1<List<Track>, Observable<List<TrackViewModel>>>() {
                    @Override
                    public Observable<List<TrackViewModel>> call(List<Track> tracks) {
                        List<TrackViewModel> tvmList = new ArrayList<>();
                        for (Track track : tracks) {
                            tvmList.add(new TrackViewModel(track.getId(),
                                                           track.getTitle(),
                                                           track.getUser().getUsername(),
                                                           convertToHighResUrl(
                                                                   track.getArtworkUrl()), 0, ""));
                        }
                        return Observable.just(tvmList);
                    }
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
