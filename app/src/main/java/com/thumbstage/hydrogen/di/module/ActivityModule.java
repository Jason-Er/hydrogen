package com.thumbstage.hydrogen.di.module;

import com.thumbstage.hydrogen.view.account.AccountActivity;
import com.thumbstage.hydrogen.view.browse.BrowseActivity;
import com.thumbstage.hydrogen.view.create.CreateActivity;
import com.thumbstage.hydrogen.view.show.ShowActivity;
import com.thumbstage.hydrogen.view.sign.SignActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract BrowseActivity contributeMainActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract CreateActivity contributeCreateActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract ShowActivity contributeShowActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract AccountActivity contributeAccountActivity();

    @ContributesAndroidInjector(modules = FragmentModule.class)
    abstract SignActivity contributeSignActivity();

}
