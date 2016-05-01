package com.moac.android.interceptordemo.fetch;

import com.moac.android.interceptordemo.TrackDataModel;
import com.moac.android.interceptordemo.api.TracksApi;
import com.moac.android.interceptordemo.api.model.Track;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.Single;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrackFetcherTest {

    @Mock
    private TracksApi mTracksApi;

    @Mock
    private TrackDataModel mTrackDataModel;

    @Mock
    private FetchConfiguration mFetchConfiguration;

    private TrackFetcher mTrackFetcher;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mTrackFetcher = new TrackFetcher(mTracksApi, mTrackDataModel, mFetchConfiguration);
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

    public void testFetch_storesApiResultInTrackDataModel() {
        List<Track> tracks = createTracks(5);
        new Arrangement().withSearchGenre("dummy")
                         .withSearchLimit(10)
                         .withApiResult(tracks);

        mTrackFetcher.fetch();

        verify(mTrackDataModel).set(eq(tracks));
    }

    @Test
    public void testCancelFetch() {

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

        Arrangement withApiResult(@NonNull final List<Track> trackResponse) {
            when(mTracksApi.getTrackList(anyString(), anyLong()))
                    .thenReturn(Single.just(trackResponse));
            return this;
        }

        Arrangement withEmptyApiResult() {
            return withApiResult(Collections.emptyList());
        }

    }

    private List<Track> createTracks(final int count) {
        return new ArrayList<>(count);
        // TODO Generate
    }
}