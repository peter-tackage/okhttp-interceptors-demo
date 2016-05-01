package com.moac.android.interceptordemo.api;

import com.moac.android.interceptordemo.api.model.Track;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Single;

import static com.moac.android.interceptordemo.api.ApiConst.GENRES_QUERY_PARAM;
import static com.moac.android.interceptordemo.api.ApiConst.LIMIT_QUERY_PARAM;
import static com.moac.android.interceptordemo.api.ApiConst.TRACKS_ENDPOINT;

public interface SoundCloudApi {

    @GET(TRACKS_ENDPOINT)
    Single<List<Track>> getTracks(@Query(GENRES_QUERY_PARAM) String genre,
                                  @Query(LIMIT_QUERY_PARAM) long limit);
}
