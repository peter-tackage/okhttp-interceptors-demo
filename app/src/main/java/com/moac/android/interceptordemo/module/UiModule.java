package com.moac.android.interceptordemo.module;

import com.moac.android.interceptordemo.TrackDataModel;
import com.moac.android.interceptordemo.activity.MainActivity;
import com.moac.android.interceptordemo.fetch.FetchScheduler;
import com.moac.android.interceptordemo.fetch.TrackFetcher;
import com.moac.android.interceptordemo.fragment.DisplayFragment;
import com.moac.android.interceptordemo.rx.ISchedulerProvider;
import com.moac.android.interceptordemo.viewmodel.TracksViewModel;

import android.app.Activity;
import android.content.Context;

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
    TracksViewModel provideTracksViewModel(TrackDataModel trackDataModel) {
        return new TracksViewModel(trackDataModel);
    }

    @Provides
    @Singleton
    FetchScheduler provideFetchScheduler(TrackFetcher trackFetcher,
                                         ISchedulerProvider schedulerProvider) {
        return new FetchScheduler(trackFetcher, schedulerProvider);
    }

}
