package com.moac.android.interceptordemo.injection.module;

import android.app.Activity;
import android.content.Context;

import com.moac.android.interceptordemo.activity.MainActivity;
import com.moac.android.interceptordemo.fragment.DisplayFragment;
import com.moac.android.interceptordemo.viewmodel.FetchScheduler;
import com.moac.android.interceptordemo.viewmodel.TracksViewModelProvider;

import java.lang.annotation.Retention;

import javax.inject.Qualifier;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Module(library = true, complete = false, injects = {MainActivity.class, DisplayFragment.class})
public class UiModule {

    private final Activity mActivity;

    public UiModule(Activity activity) {
        mActivity = activity;
    }

    @Provides
    @Singleton
    @ForActivity
    Context provideActivityContext() {
        return mActivity;
    }

    @Provides
    @Singleton
    @ForActivity
    Activity provideActivity() {
        return mActivity;
    }

    @Qualifier
    @Retention(RUNTIME)
    public @interface ForActivity {
    }

    @Provides
    @Singleton
    TracksViewModelProvider provideTracksViewModelProvider() {
        return new TracksViewModelProvider();
    }

    @Provides
    @Singleton
    FetchScheduler provideFetchScheduler() {
        return new FetchScheduler();
    }

}
