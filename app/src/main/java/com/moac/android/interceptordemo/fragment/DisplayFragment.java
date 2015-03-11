package com.moac.android.interceptordemo.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.moac.android.interceptordemo.R;
import com.moac.android.interceptordemo.api.model.Track;
import com.moac.android.interceptordemo.injection.InjectingFragment;
import com.moac.android.interceptordemo.provider.TracksProvider;
import com.moac.android.interceptordemo.rx.SimpleObserver;
import com.moac.android.interceptordemo.viewmodel.FetchScheduler;
import com.moac.android.interceptordemo.viewmodel.TrackFetcher;
import com.moac.android.interceptordemo.viewmodel.TrackViewModel;
import com.moac.android.interceptordemo.viewmodel.TracksViewModelProvider;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.functions.Action0;
import rx.functions.Action1;

public class DisplayFragment extends InjectingFragment {

    private static final String TAG = DisplayFragment.class.getSimpleName();

    @Inject
    TracksViewModelProvider mViewModelProvider;

    @Inject
    Picasso mPicasso;

    @Inject
    FetchScheduler mFetchScheduler;

    @Inject
    TracksProvider mTracksProvider;

    private Subscription mViewModelSubscription;
    private TextView mArtistTextView;
    private TextView mTitleTextView;
    private ImageView mTrackImageView;

    // Configuration
    private Observable<String> mGenre = Observable.just("ambient");
    private Observable<Long> mLimit = Observable.just(15l);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_display, container, false);
        mArtistTextView = (TextView) rootView.findViewById(R.id.textView_artist);
        mTitleTextView = (TextView) rootView.findViewById(R.id.textView_title);
        mTrackImageView = (ImageView) rootView.findViewById(R.id.imageView_track);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModelSubscription =
                AppObservable.bindFragment(this,
                        mViewModelProvider.getObservableViewModel(5, TimeUnit.SECONDS))
                        .subscribe(new Action1<TrackViewModel>() {
                            @Override
                            public void call(TrackViewModel trackViewModel) {
                                Log.i(TAG, "Rendering new track: " + trackViewModel);
                                mPicasso.load(trackViewModel.getArtworkUrl())
                                        .into(mTrackImageView);
                                mArtistTextView.setText(trackViewModel.getArtist());
                                mTitleTextView.setText(trackViewModel.getTitle());
                            }
                        }, new Action1<Throwable>() {
                            @Override
                            public void call(Throwable throwable) {
                                Log.e(TAG, "ViewModelProvider Observable errorred!", throwable);
                            }
                        }, new Action0() {
                            @Override
                            public void call() {
                                Log.w(TAG, "ViewModelProvider Observable completed!");
                            }
                        });


        // Fetch a new set of tracks every 1 minute
        mFetchScheduler.start(20, TimeUnit.SECONDS,
                new TrackFetcher(mTracksProvider,
                        mViewModelProvider.asDestination(),
                        new SimpleObserver<List<Track>>() {
                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        },
                        mGenre,
                        mLimit, "DisplayFragment|Fetcher"
                ));

    }

    @Override
    public void onStop() {
        super.onStop();
        mViewModelSubscription.unsubscribe();
        mFetchScheduler.stop();
        // TODO Stop fetcher
    }
}
