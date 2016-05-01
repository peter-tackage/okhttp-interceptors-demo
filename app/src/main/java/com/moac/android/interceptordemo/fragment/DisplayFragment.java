package com.moac.android.interceptordemo.fragment;

import com.moac.android.interceptordemo.R;
import com.moac.android.interceptordemo.api.model.Track;
import com.moac.android.interceptordemo.injection.InjectingFragment;
import com.moac.android.interceptordemo.provider.TracksProvider;
import com.moac.android.interceptordemo.rx.SimpleObserver;
import com.moac.android.interceptordemo.viewmodel.FetchScheduler;
import com.moac.android.interceptordemo.viewmodel.TrackFetcher;
import com.moac.android.interceptordemo.viewmodel.TracksViewModelProvider;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

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
    private Observable<Long> mLimit = Observable.just(15L); // fetch 15 tracks

    private TrackFetcher mTrackFetcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                mViewModelProvider.getObservableViewModel(5, TimeUnit.SECONDS)
                                  .observeOn(AndroidSchedulers.mainThread())
                                  .subscribe(trackViewModel -> {
                                                 Log.i(TAG, "Rendering new track: " + trackViewModel);
                                                 mPicasso.load(trackViewModel.getArtworkUrl())
                                                         .into(mTrackImageView);
                                                 mArtistTextView.setText(trackViewModel.getArtist());
                                                 mTitleTextView.setText(trackViewModel.getTitle());
                                             },
                                             throwable -> Log
                                                     .e(TAG,
                                                        "ViewModelProvider Observable errorred!",
                                                        throwable),
                                             () -> Log.w(TAG,
                                                         "ViewModelProvider Observable completed!"));

        // Configure Track Fetcher
        mTrackFetcher = new TrackFetcher(mTracksProvider,
                                         mViewModelProvider.asDestination(),
                                         new SimpleObserver<List<Track>>() {
                                             @Override
                                             public void onError(Throwable e) {
                                                 Toast.makeText(getActivity(),
                                                                "Error - " + e.getMessage(),
                                                                Toast.LENGTH_SHORT).show();
                                             }
                                         },
                                         mGenre,
                                         mLimit, "DisplayFragment|Fetcher"
        );
        // Fetch a new set of tracks every 20 seconds
        mFetchScheduler.start(20, TimeUnit.SECONDS, mTrackFetcher);

    }

    @Override
    public void onStop() {
        super.onStop();
        mViewModelSubscription.unsubscribe();
        mTrackFetcher.cancelFetch(); // cancel inflight
        mFetchScheduler.stop();
    }
}
