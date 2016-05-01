package com.moac.android.interceptordemo.fetch;

import com.moac.android.interceptordemo.test.TestSchedulerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.TimeUnit;

import rx.schedulers.TestScheduler;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class FetchSchedulerTest {

    @Mock
    private TrackFetcher mTrackFetcher;

    private TestScheduler mTestScheduler;

    private FetchScheduler mFetchScheduler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mTestScheduler = new TestScheduler();
        mFetchScheduler = new FetchScheduler(mTrackFetcher,
                                             new TestSchedulerProvider(mTestScheduler));
    }

    @Test
    public void testStart_callsTrackFetchInitially() {
        mFetchScheduler.start(500, TimeUnit.DAYS);
        mTestScheduler.triggerActions();

        verify(mTrackFetcher).fetch();
    }

    @Test
    public void testStart_callsTrackFetchAgainAfterDelay() {
        int interval = 500;
        TimeUnit timeUnit = TimeUnit.DAYS;
        mFetchScheduler.start(interval, timeUnit);
        mTestScheduler.advanceTimeBy(interval, timeUnit);
        mTestScheduler.triggerActions();

        verify(mTrackFetcher, times(2)).fetch();
    }

    @Test
    public void testStop_cancelsTrackFetch() {
        mFetchScheduler.stop();

        verify(mTrackFetcher).cancelFetch();
    }

    @Test
    public void testStart_callsTrackFetch_whenStartAfterStop() {
        mFetchScheduler.stop();

        mFetchScheduler.start(500, TimeUnit.DAYS);
        mTestScheduler.triggerActions();

        verify(mTrackFetcher).fetch();
    }
}
