package com.moac.android.interceptordemo.api;

import retrofit.RequestInterceptor;

public class SoundCloudRequestInterceptor implements RequestInterceptor {

    private final String mClientId;

    public SoundCloudRequestInterceptor(String clientId) {
        mClientId = clientId;
    }

    @Override
    public void intercept(RequestInterceptor.RequestFacade request) {
        // Add client id to request
        request.addEncodedQueryParam(ApiConst.CLIENT_ID_QUERY_PARAM, mClientId);
    }
}

