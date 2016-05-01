package com.moac.android.interceptordemo.api;

import com.moac.android.interceptordemo.api.model.Track;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

import static com.moac.android.interceptordemo.api.ApiConst.GENRES_QUERY_PARAM;
import static com.moac.android.interceptordemo.api.ApiConst.LIMIT_QUERY_PARAM;
import static com.moac.android.interceptordemo.api.ApiConst.TRACKS_ENDPOINT;

public interface SoundCloudApi {

    @GET(TRACKS_ENDPOINT)
    Observable<List<Track>> getTracks(@Query(GENRES_QUERY_PARAM) String genre,
                                      @Query(LIMIT_QUERY_PARAM) long limit);
}
