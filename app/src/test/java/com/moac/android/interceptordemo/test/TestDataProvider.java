package com.moac.android.interceptordemo.test;

import com.moac.android.interceptordemo.api.model.Track;
import com.moac.android.interceptordemo.api.model.User;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public final class TestDataProvider {

    @NonNull
    public static List<Track> createTracks(final int count) {
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

    @NonNull
    private static User dummyUser(final int position) {
        return User.create(formatted("id", position),
                           formatted("username", position),
                           formatted("uri", position));
    }

    @NonNull
    private static String formatted(@NonNull final String string, final int position) {
        return String.format(string + "%d", position);
    }

    private TestDataProvider() {
        throw new AssertionError("No instances.");
    }
}
