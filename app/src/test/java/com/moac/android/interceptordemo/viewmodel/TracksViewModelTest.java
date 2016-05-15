package com.moac.android.interceptordemo.viewmodel;

import com.moac.android.interceptordemo.TrackDataModel;
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
import static org.assertj.core.api.Assertions.assertThat;
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
    public void testGetTrackDataStream_EmitsFirstTrackImmediately() {
        List<Track> tracks = createTracks(5);
        new Arrangement().withDataModelTracks(tracks);
        TestSubscriber<Track> ts = new TestSubscriber<>();
        int dummyDisplayInterval = 100;

        mTracksViewModel.getTrackDataStream(dummyDisplayInterval, TimeUnit.DAYS).subscribe(ts);
        mTestScheduler.triggerActions();

        ts.assertNoTerminalEvent();
        ts.assertValue(tracks.get(0));
    }

    @Test
    public void testGetTrackDataStream_emitsSecondTrackAfterDisplayInterval() {
        List<Track> tracks = createTracks(5);
        new Arrangement().withDataModelTracks(tracks);
        TestSubscriber<Track> ts = new TestSubscriber<>();
        int displayInterval = 100;

        mTracksViewModel.getTrackDataStream(displayInterval, TimeUnit.DAYS).subscribe(ts);
        mTestScheduler.advanceTimeBy(displayInterval, TimeUnit.DAYS);
        mTestScheduler.triggerActions();

        ts.assertNoTerminalEvent();
        ts.assertValues(tracks.get(0), tracks.get(1));
    }

    @Test
    public void testGetTrackDataStream_emitsThirdTrackAfterDisplayInterval() {
        List<Track> tracks = createTracks(5);
        new Arrangement().withDataModelTracks(tracks);
        TestSubscriber<Track> ts = new TestSubscriber<>();
        int displayInterval = 100;

        mTracksViewModel.getTrackDataStream(displayInterval, TimeUnit.DAYS).subscribe(ts);
        mTestScheduler.advanceTimeBy(displayInterval * 2, TimeUnit.DAYS);
        mTestScheduler.triggerActions();

        ts.assertNoTerminalEvent();
        ts.assertValues(tracks.get(0), tracks.get(1), tracks.get(2));
    }

    @Test
    public void testGetTrackDataStream_emitsAllTrackAfterEachDisplayInterval() {
        List<Track> tracks = createTracks(5);
        new Arrangement().withDataModelTracks(tracks);
        TestSubscriber<Track> ts = new TestSubscriber<>();
        int displayInterval = 100;

        mTracksViewModel.getTrackDataStream(displayInterval, TimeUnit.DAYS).subscribe(ts);
        mTestScheduler.advanceTimeBy(displayInterval * ((tracks.size() - 1)), TimeUnit.DAYS);
        mTestScheduler.triggerActions();

        ts.assertNoTerminalEvent();
        ts.assertValues(tracks.get(0), tracks.get(1), tracks.get(2), tracks.get(3), tracks.get(4));
    }

    @Test
    public void testGetTrackDataStream_repeatsFirstTrackAfterAllDisplayIntervals() {
        List<Track> tracks = createTracks(5);
        new Arrangement().withDataModelTracks(tracks);
        TestSubscriber<Track> ts = new TestSubscriber<>();
        int displayInterval = 100;

        mTracksViewModel.getTrackDataStream(displayInterval, TimeUnit.DAYS).subscribe(ts);
        mTestScheduler.advanceTimeBy(displayInterval * tracks.size(), TimeUnit.DAYS);
        mTestScheduler.triggerActions();

        ts.assertNoTerminalEvent();
        System.out.println(ts.getOnNextEvents());
        ts.assertValueCount(tracks.size() + 1);
        assertThat(ts.getOnNextEvents().get(tracks.size())).isEqualTo(tracks.get(0));
    }

    @Test
    public void testGetTrackDataStream_emitNewTracksWhenReceived() {
        List<Track> firstTrackList = createTracks(5);
        List<Track> secondTrackList = createTracks(6, 10);
        Arrangement arrangement = new Arrangement().withDataModelTracks(firstTrackList);
        TestSubscriber<Track> ts = new TestSubscriber<>();
        int displayInterval = 100;
        int beforeDisplayInterval = displayInterval / 2;
        int afterDisplayInterval = beforeDisplayInterval + displayInterval;

        mTracksViewModel.getTrackDataStream(displayInterval, TimeUnit.DAYS).subscribe(ts);
        mTestScheduler.advanceTimeBy(beforeDisplayInterval, TimeUnit.DAYS);
        arrangement.action().enqueueDataModelTracks(secondTrackList);
        mTestScheduler.advanceTimeBy(afterDisplayInterval, TimeUnit.DAYS);
        mTestScheduler.triggerActions();

        ts.assertNoTerminalEvent();
        ts.assertValues(firstTrackList.get(0), secondTrackList.get(0), secondTrackList.get(1));
    }

    private class Arrangement {

        @NonNull
        private final BehaviorSubject<List<Track>> mTracksOnceAndStream = BehaviorSubject.create();

        @NonNull
        private final Action mAction;

        public Arrangement() {
            mAction = new Action(mTracksOnceAndStream);
        }

        @NonNull
        Arrangement withDataModelTracks(@NonNull final List<Track> tracks) {
            mAction.enqueueDataModelTracks(tracks);
            when(mTrackDataModel.getTracksOnceAndStream()).thenReturn(mTracksOnceAndStream);
            return this;
        }

        @NonNull
        Action action() {
            return mAction;
        }

    }

    private class Action {

        private final BehaviorSubject<List<Track>> mTracksOnceAndStream;

        public Action(@NonNull final BehaviorSubject<List<Track>> tracksOnceAndStream) {
            mTracksOnceAndStream = tracksOnceAndStream;
        }

        void enqueueDataModelTracks(@NonNull final List<Track> apiTracks) {
            mTracksOnceAndStream.onNext(apiTracks);
        }
    }
}
