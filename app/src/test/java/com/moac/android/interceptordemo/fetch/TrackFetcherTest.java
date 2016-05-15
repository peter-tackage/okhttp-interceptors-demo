package com.moac.android.interceptordemo.fetch;

import com.moac.android.interceptordemo.TrackDataModel;
import com.moac.android.interceptordemo.api.IArtworkUrlConverter;
import com.moac.android.interceptordemo.api.TracksApi;
import com.moac.android.interceptordemo.api.model.ApiTrack;
import com.moac.android.interceptordemo.viewmodel.Track;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Single;
import rx.schedulers.TestScheduler;

import static com.moac.android.interceptordemo.test.TestDataProvider.createApiTracks;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
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

    @Mock
    private IArtworkUrlConverter mArtworkUrlConverter;

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
    public void testFetch_invokesArtworkConversionForEachTrack() {
        List<ApiTrack> apiTracks = createApiTracks(5);
        new Arrangement().withSearchGenre("dummy")
                         .withSearchLimit(10)
                         .withSuccessfulArtworkConversion()
                         .withApiResult(apiTracks);

        mTrackFetcher.fetch();

        verify(mArtworkUrlConverter, times(5)).convertToHighResUrl(anyString());
    }

    @Test
    public void testFetch_invokesArtworkConversionUsingArtworkUrl() {
        List<ApiTrack> apiTracks = createApiTracks(1);
        new Arrangement().withSearchGenre("dummy")
                         .withSearchLimit(10)
                         .withSuccessfulArtworkConversion()
                         .withApiResult(apiTracks);

        mTrackFetcher.fetch();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mArtworkUrlConverter).convertToHighResUrl(captor.capture());
        assertThat(captor.getValue()).isEqualTo(apiTracks.get(0).artworkUrl());
    }

    @Test
    public void testFetch_invokesArtworkConversionForEachTrackUsingArtworkUrl() {
        List<ApiTrack> apiTracks = createApiTracks(3);
        new Arrangement().withSearchGenre("dummy")
                         .withSearchLimit(10)
                         .withSuccessfulArtworkConversion()
                         .withApiResult(apiTracks);

        mTrackFetcher.fetch();

        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mArtworkUrlConverter, times(3)).convertToHighResUrl(captor.capture());
        List<String> invocatedParams = captor.getAllValues();
        assertThat(invocatedParams.get(0)).isEqualTo(apiTracks.get(0).artworkUrl());
        assertThat(invocatedParams.get(1)).isEqualTo(apiTracks.get(1).artworkUrl());
        assertThat(invocatedParams.get(2)).isEqualTo(apiTracks.get(2).artworkUrl());
    }

    @Test
    public void testFetch_storesApiResultTracksInTrackDataModel_whenSuccessfulArtworkConversion() {
        List<ApiTrack> apiTracks = createApiTracks(5);
        new Arrangement().withSearchGenre("dummy")
                         .withSearchLimit(10)
                         .withSuccessfulArtworkConversion()
                         .withApiResult(apiTracks);

        mTrackFetcher.fetch();

        verify(mTrackDataModel).set(anyListOf(Track.class));
    }

    @Test
    public void testFetch_storesAllApiResultTracksInTrackDataModel_whenSuccessfulArtworkConversion() {
        List<ApiTrack> apiTracks = createApiTracks(5);
        new Arrangement().withSearchGenre("dummy")
                         .withSearchLimit(10)
                         .withSuccessfulArtworkConversion()
                         .withApiResult(apiTracks);

        mTrackFetcher.fetch();

        @SuppressWarnings("unchecked")
        ArgumentCaptor<List<Track>> captor = ArgumentCaptor.forClass(List.class);
        verify(mTrackDataModel).set(captor.capture());
        assertThat(captor.getAllValues()).hasSize(1);
        assertThat(captor.getValue()).hasSize(apiTracks.size());
    }

    @Test
    public void testFetch_filtersApiTrack_whenNotSuccessfulArtworkConversion() {
        List<ApiTrack> apiTracks = createApiTracks(5);
        new Arrangement().withSearchGenre("dummy")
                         .withSearchLimit(10)
                         .withFailedArtworkConversion()
                         .withApiResult(apiTracks);

        mTrackFetcher.fetch();

        verify(mTrackDataModel, never()).set(any());
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
                         .withApiResult(createApiTracks(5))
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
                         .withApiResult(Single.just(createApiTracks(5))
                                              .delay(2, TimeUnit.DAYS, testScheduler)
                                              .doOnUnsubscribe(unsubscribeLatch::countDown))
                         .withAction().fetch();

        testScheduler.advanceTimeBy(1, TimeUnit.DAYS);
        assertThat(unsubscribeLatch.getCount()).isEqualTo(1);
        mTrackFetcher.cancelFetch();

        assertThat(unsubscribeLatch.await(5, TimeUnit.SECONDS)).isTrue();
    }

    // Arrangement

    private class Arrangement {

        @NonNull
        Arrangement withSearchGenres(@NonNull final Observable<String> genres) {
            when(mFetchConfiguration.getGenreStream()).thenReturn(genres);
            return this;
        }

        @NonNull
        Arrangement withSearchLimits(@NonNull final Observable<Long> limits) {
            when(mFetchConfiguration.getLimitStream()).thenReturn(limits);
            return this;
        }

        @NonNull
        Arrangement withSearchGenre(@NonNull final String genre) {
            return withSearchGenres(Observable.just(genre));
        }

        @NonNull
        Arrangement withSearchLimit(final long limit) {
            return withSearchLimits(Observable.just(limit));
        }

        @NonNull
        Arrangement withApiResult(@NonNull final Single<List<ApiTrack>> trackResponse) {
            when(mTracksApi.getTrackList(anyString(), anyLong()))
                    .thenReturn(trackResponse);
            return this;
        }

        @NonNull
        Arrangement withApiResult(@NonNull final List<ApiTrack> apiTrackResponse) {
            return withApiResult(Single.just(apiTrackResponse));
        }

        @NonNull
        Arrangement withSuccessfulArtworkConversion() {
            when(mArtworkUrlConverter.convertToHighResUrl(anyString()))
                    .thenReturn("dummyHisResUrl");
            return this;
        }

        @NonNull
        Arrangement withFailedArtworkConversion() {
            when(mArtworkUrlConverter.convertToHighResUrl(anyString())).thenReturn(null);
            return this;
        }

        @NonNull
        Arrangement withEmptyApiResult() {
            return withApiResult(Collections.emptyList());
        }

        @NonNull
        Action withAction() {
            return new Action();
        }

    }

    private class Action {

        @NonNull
        Action fetch() {
            mTrackFetcher.fetch();
            return this;
        }

        @NonNull
        Action cancelFetch() {
            mTrackFetcher.cancelFetch();
            return this;
        }
    }

}
