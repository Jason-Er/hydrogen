package com.thumbstage.hydrogen.di.module;

import com.thumbstage.hydrogen.view.browse.BrowseActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract BrowseActivity contributeMainActivity();
}
