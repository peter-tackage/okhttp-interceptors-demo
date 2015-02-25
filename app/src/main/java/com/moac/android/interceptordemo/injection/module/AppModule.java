package com.moac.android.interceptordemo.injection.module;

import android.app.Application;
import android.content.Context;

import com.moac.android.interceptordemo.DemoApplication;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;

import dagger.Module;
import dagger.Provides;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/*
 * Top level app specific modules
 */
@Module(library = true,
        includes = {ConfigModule.class, NetworkModule.class, ApiModule.class, ImagesModule.class, LoggingModule.class},
        injects = DemoApplication.class)
public class AppModule {

    private final Application mApplication;

    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ForApplication
    public Context provideApplicationContext() {
        return mApplication.getApplicationContext();
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface ForApplication {
    }
}
