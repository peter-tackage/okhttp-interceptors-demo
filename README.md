okhttp-interceptor-demo
=======================

Note: To run this app, you will require your own SoundCloud API key.

This is an app to demonstrate the use of OkHttp Interceptors and Facebook's Stetho library.

It also uses RxJava, Dagger, Retrofit and Picasso.

The app makes two forms of network calls:

1. Every 20 seconds, it retrieves a set of tracks (songs) from SoundCloud API via Retrofit.
2. Every 5 seconds, it changes the displayed track and retrieves the associated artwork via Picasso.

Two instances of OkHttp clients are created. One for Retrofit and another for Picasso. While not required,
this gives a demonstration of how to fix them independent configurations. 

The app has a small suite of it's own OkHttp Interceptors:

* LoggingInterceptor - basic logging (derived from OkHttp Recipes)
* NeverCacheInterceptor - removes all caching headers from the response, forcing networking reloading
* AssertNoCacheInterceptor - reports an error if the response is cached (has cache headers)

It also registers an instance of the Facebook StethoInterceptor as a Network Interceptor to provide visibility to the network requests and responses.

The Interceptors in use are defined in the InterceptorModule class, with the registration is performed in the ApiModule and ImagesModule classes.

The ConfigModule provides a range of named configuration parameters required by the other modules. This makes the Module act like a properties file.

**Screenshots**
![An example screenshot](https://github.com/peter-tackage/assets/raw/master/screenshots/okhttp-interceptor-demo/Screenshot_2015-03-31-23-07-20.png)