package com.moac.android.interceptordemo.viewmodel;

import com.moac.android.interceptordemo.TrackDataModel;
import com.moac.android.interceptordemo.api.model.Track;
import com.moac.android.interceptordemo.rx.ISchedulerProvider;
import com.moac.android.interceptordemo.test.TestSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;
import rx.subjects.BehaviorSubject;

import static com.moac.android.interceptordemo.test.TestDataProvider.createTracks;
import static org.mockito.Mockito.when;

public final class TracksViewModelTest {

    @Mock
    private TrackDataModel mTrackDataModel;

    private TestScheduler mTestScheduler;

    private TracksViewModel mTracksViewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mTestScheduler = new TestScheduler();
        ISchedulerProvider schedulerProvider = new TestSchedulerProvider(mTestScheduler);

        mTracksViewModel = new TracksViewModel(mTrackDataModel, schedulerProvider);
    }

    @Test
    public void testGetTrackDataStream_EmitfirstTrack_immediately() {
        new Arrangement().withDataModelTracks(createTracks(5));

        TestSubscriber<TrackData> ts = new TestSubscriber<>();
        mTracksViewModel.getTrackDataStream(100, TimeUnit.DAYS).subscribe(ts);
        //mTestScheduler.triggerActions();

        ts.assertNoTerminalEvent();
        ts.assertValueCount(1);
    }

    private class Arrangement {

        private BehaviorSubject<List<Track>> tracksOnceAndStream = BehaviorSubject.create();

        Arrangement withDataModelTracks(@NonNull final List<Track> tracks) {
            enqueueDataModelTracks(tracks);
            when(mTrackDataModel.getTracksOnceAndStream()).thenReturn(tracksOnceAndStream);
            return this;
        }

        void enqueueDataModelTracks(@NonNull final List<Track> tracks) {
            tracksOnceAndStream.onNext(tracks);
        }
    }
}
