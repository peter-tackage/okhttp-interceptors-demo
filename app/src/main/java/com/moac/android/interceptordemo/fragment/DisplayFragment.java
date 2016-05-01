package com.moac.android.interceptordemo.fragment;

import com.moac.android.interceptordemo.R;
import com.moac.android.interceptordemo.api.TracksApi;
import com.moac.android.interceptordemo.fetch.FetchScheduler;
import com.moac.android.interceptordemo.injection.InjectingFragment;
import com.moac.android.interceptordemo.viewmodel.TrackData;
import com.moac.android.interceptordemo.viewmodel.TracksViewModel;
import com.squareup.picasso.Picasso;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class DisplayFragment extends InjectingFragment {

    private static final String TAG = DisplayFragment.class.getSimpleName();

    @Inject
    TracksViewModel mViewModelProvider;

    @Inject
    Picasso mPicasso;

    @Inject
    FetchScheduler mFetchScheduler;

    @Inject
    TracksApi mTracksApi;

    private CompositeSubscription mViewModelSubscription;
    private TextView mArtistTextView;
    private TextView mTitleTextView;
    private ImageView mTrackImageView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
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
        mViewModelSubscription = new CompositeSubscription();

        // Update the displayed track every 5 seconds
        mViewModelSubscription.add(mViewModelProvider.getTrackDataStream(5, TimeUnit.SECONDS)
                                                     .observeOn(AndroidSchedulers.mainThread())
                                                     .subscribe(this::renderTrack,
                                                                DisplayFragment::logError,
                                                                DisplayFragment::logCompleted));

        // Fetch a new set of tracks every 20 seconds
        mFetchScheduler.start(20, TimeUnit.SECONDS);
    }

    @Override
    public void onStop() {
        super.onStop();
        mViewModelSubscription.clear();
        mFetchScheduler.stop();
    }

    private void renderTrack(@NonNull final TrackData trackData) {
        Log.i(TAG, "Rendering new track: " + trackData);
        mPicasso.load(trackData.getArtworkUrl())
                .into(mTrackImageView);
        mArtistTextView.setText(trackData.getArtist());
        mTitleTextView.setText(trackData.getTitle());
    }

    private static int logCompleted() {
        return Log.w(TAG, "ViewModelProvider Observable completed!");
    }

    private static int logError(final Throwable throwable) {
        return Log.e(TAG, "ViewModelProvider Observable errored!", throwable);
    }

}
