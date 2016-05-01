package com.moac.android.interceptordemo.api.model;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

public class AutoValueGsonTypeAdapterFactory implements TypeAdapterFactory {

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        Class<? super T> rawType = type.getRawType();
        if (rawType.equals(Track.class)) {
            return (TypeAdapter<T>) Track.typeAdapter(gson);
        } else if (rawType.equals(User.class)) {
            return (TypeAdapter<T>) User.typeAdapter(gson);
        }
        return null;
    }
}
