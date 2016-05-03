package com.moac.android.interceptordemo.fetch;

import com.moac.android.interceptordemo.TrackDataModel;
import com.moac.android.interceptordemo.api.TracksApi;
import com.moac.android.interceptordemo.api.model.Track;
import com.moac.android.interceptordemo.api.model.User;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;
import rx.schedulers.TestScheduler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrackFetcherTest {

    @Mock
    private TracksApi mTracksApi;

    @Mock
    private TrackDataModel mTrackDataModel;

    @Mock
    private FetchConfiguration mFetchConfiguration;

    @InjectMocks
    private TrackFetcher mTrackFetcher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testNoFetch_doesNotInvokesApi() {
        new Arrangement().withSearchGenre("dummy")
                         .withSearchLimit(123)
                         .withEmptyApiResult();

        verify(mTracksApi, never()).getTrackList(anyString(), anyLong());
    }

    @Test
    public void testFetch_invokesApiWithInitialFetchConfigurationValues() {
        final String genre = "jazz";
        final long limit = 100;
        new Arrangement().withSearchGenre(genre)
                         .withSearchLimit(limit)
                         .withEmptyApiResult();

        mTrackFetcher.fetch();

        verify(mTracksApi).getTrackList(eq(genre), eq(limit));
    }

    @Test
    public void testFetch_storesApiResultInTrackDataModel() {
        List<Track> tracks = createTracks(5);
        new Arrangement().withSearchGenre("dummy")
                         .withSearchLimit(10)
                         .withApiResult(tracks);

        mTrackFetcher.fetch();

        verify(mTrackDataModel).set(eq(tracks));
    }

    @Test
    public void testFetch_allowsRefetch_whenError() {
        new Arrangement().withSearchGenre("dummy")
                         .withSearchLimit(10)
                         .withApiResult(Single.error(new Exception()))
                         .withAction().fetch();

        mTrackFetcher.fetch();

        verify(mTracksApi, times(2)).getTrackList(anyString(), anyLong());
    }

    @Test
    public void testCancelFetch_allowsRefetch() {
        new Arrangement().withSearchGenre("dummy")
                         .withSearchLimit(10)
                         .withApiResult(createTracks(5))
                         .withAction().fetch().cancelFetch();

        mTrackFetcher.fetch();

        verify(mTracksApi, times(2)).getTrackList(anyString(), anyLong());
    }

    @Test
    public void testCancelFetch_unsubscribesFromApi() throws InterruptedException {
        final TestScheduler testScheduler = new TestScheduler();
        final CountDownLatch unsubscribeLatch = new CountDownLatch(1);
        new Arrangement().withSearchGenre("dummy")
                         .withSearchLimit(10)
                         .withApiResult(Single.just(createTracks(5))
                                              .delay(2, TimeUnit.DAYS, testScheduler)
                                              .doOnUnsubscribe(unsubscribeLatch::countDown))
                         .withAction().fetch();

        testScheduler.advanceTimeBy(1, TimeUnit.DAYS);
        assertThat(unsubscribeLatch.getCount()).isEqualTo(1);
        mTrackFetcher.cancelFetch();

        assertThat(unsubscribeLatch.await(5, TimeUnit.SECONDS)).isTrue();
    }

    private class Arrangement {

        Arrangement withSearchGenres(@NonNull final Observable<String> genres) {
            when(mFetchConfiguration.getGenreStream()).thenReturn(genres);
            return this;
        }

        Arrangement withSearchLimits(final Observable<Long> limits) {
            when(mFetchConfiguration.getLimitStream()).thenReturn(limits);
            return this;
        }

        Arrangement withSearchGenre(@NonNull final String genre) {
            return withSearchGenres(Observable.just(genre));
        }

        Arrangement withSearchLimit(final long limit) {
            return withSearchLimits(Observable.just(limit));
        }

        Arrangement withApiResult(@NonNull final Single<List<Track>> trackResponse) {
            when(mTracksApi.getTrackList(anyString(), anyLong()))
                    .thenReturn(trackResponse);
            return this;
        }

        Arrangement withApiResult(@NonNull final List<Track> trackResponse) {
            when(mTracksApi.getTrackList(anyString(), anyLong()))
                    .thenReturn(Single.just(trackResponse));
            return this;
        }

        Arrangement withEmptyApiResult() {
            return withApiResult(Collections.emptyList());
        }

        Action withAction() {
            return new Action();
        }

    }

    private class Action {

        Action fetch() {
            mTrackFetcher.fetch();
            return this;
        }

        Action cancelFetch() {
            mTrackFetcher.cancelFetch();
            return this;
        }
    }

    private static List<Track> createTracks(final int count) {
        ArrayList<Track> tracks = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            tracks.add(dummyTrack(i));
        }
        return tracks;
    }

    @NonNull
    private static Track dummyTrack(final int position) {
        return Track.create(position,
                            formatted("title", position),
                            dummyUser(position),
                            formatted("artworkUrl", position));
    }

    private static User dummyUser(final int position) {
        return User.create(formatted("id", position),
                           formatted("username", position),
                           formatted("uri", position));
    }

    private static String formatted(@NonNull final String string, final int position) {
        return String.format(string + "%d", position);
    }
}