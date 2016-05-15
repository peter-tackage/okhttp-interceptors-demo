package com.moac.android.interceptordemo.test;

import com.moac.android.interceptordemo.api.model.ApiTrack;
import com.moac.android.interceptordemo.api.model.ApiUser;
import com.moac.android.interceptordemo.viewmodel.Track;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class TestDataProvider {

    @NonNull
    public static List<Track> createTracks(final int count) {
        return createTracks(1, count);
    }

    @NonNull
    public static List<Track> createTracks(final int from, final int to) {
        ArrayList<Track> tracks = new ArrayList<>(to - from);
        for (int i = from; i <= to; i++) {
            tracks.add(dummyTrack(i));
        }
        return tracks;
    }

    @NonNull
    public static List<ApiTrack> createApiTracks(final int count) {
        ArrayList<ApiTrack> apiTracks = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            apiTracks.add(dummyApiTrack(i));
        }
        return apiTracks;
    }

    @NonNull
    private static ApiTrack dummyApiTrack(final int position) {
        return ApiTrack.create(position,
                               formattedValue("title", position),
                               dummyUser(position),
                               formattedValue("artworkUrl", position));
    }

    @NonNull
    private static Track dummyTrack(final int position) {
        return Track.create(position,
                            formattedValue("title", position),
                            formattedValue("artist", position),
                            formattedValue("artworkUrl", position),
                            position,
                            formattedValue("duration", position));
    }

    @NonNull
    private static ApiUser dummyUser(final int position) {
        return ApiUser.create(formattedValue("id", position),
                              formattedValue("username", position),
                              formattedValue("uri", position));
    }

    @NonNull
    private static String formattedValue(@NonNull final String string, final int position) {
        return String.format(string + "%d", position);
    }

    private TestDataProvider() {
        throw new AssertionError("No instances.");
    }
}
