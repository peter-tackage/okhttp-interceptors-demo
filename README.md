okhttp-interceptor-demo
=======================

This app was used to demonstrate the use of OkHttp Interceptors and Facebook's Stetho library during
 my talk given as a part of the Futurice Android meetup in Helsinki on 12th March 2015.

Note: To run this app, you will require your own SoundCloud API key.

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

License
-------

    Copyright 2015 Peter Tackage

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.