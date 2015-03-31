package com.moac.android.interceptordemo.interceptor;

import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Response;

import java.io.IOException;

/*
 * FIXME This doesn't accurately measure the bandwidth
 */
public class BandwidthLimitingInterceptor implements Interceptor {

    private static final String TAG = BandwidthLimitingInterceptor.class.getSimpleName();
    public static final String CONTENT_LENGTH_HEADER = "Content-Length";

    private final long mBandwidthLimitInBytes;
    private long mUsedBandwidth;

    public BandwidthLimitingInterceptor(long maxBandwidthInBytes) {
        mBandwidthLimitInBytes = maxBandwidthInBytes;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        // This cheap and nasty way of doing this as it will only stop calls from
        // proceeding AFTER the previous call has exceeded the limit.
        Log.v(TAG, String.format("Requesting: %s, used bandwidth: %d/%d", chain.request().urlString(), mUsedBandwidth, mBandwidthLimitInBytes));
        if (mUsedBandwidth >= mBandwidthLimitInBytes) {
            throw new IllegalStateException("Maximum bandwidth exceeded");
        }

        Response response = chain.proceed(chain.request());

        // This doesn't work with chunked Transfer-Encoding because no Content-Length header is sent

        // Assumes fixed content length in response
        long bodyContentLength = response.body().contentLength();
        long headerContentLength = response.headers().names().contains(CONTENT_LENGTH_HEADER) ?
                Long.valueOf(response.headers().get("Content-Length"))
                : -1;
        if (bodyContentLength >= 0 || headerContentLength >= 0) {
            Log.v(TAG, String.format("Content length for %s is %d or %d", chain.request().urlString(), headerContentLength, bodyContentLength));
            //if (contentLength != -1L) {
            mUsedBandwidth += Math.max(headerContentLength, bodyContentLength);
            //  }
        }
        return response;
    }
}