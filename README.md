okhttp-interceptor-demo
=======================

This is an app to demonstrate the use of OkHttp Intereptors and Facebook's Stetho library.

The app makes two forms of network calls:

1. Every 20 seconds it retrieves a set of tracks (songs) from SoundCloud API via Retrofit.
2. Every 5 seconds, it changes the displayed track and retrieves the associated artwork via Picasso.

The app currently has a small suite of it's own OkHttp Interceptors:

* LoggingInterceptor
* NeverCacheInterceptor
* AssertNoCacheInterceptor
* BandwidthLimitingInterceptor

It also registers an instance of the Facebook StethoInterceptor as a Network Interceptor to provide visibility to the network requests and responses.

The Interceptors in use are defined in the InterceptorModule class, with the registration is performed in the ApiModule and ImagesModule classes.
