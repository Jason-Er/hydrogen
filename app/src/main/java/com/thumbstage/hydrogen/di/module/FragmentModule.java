package com.thumbstage.hydrogen.di.module;


import com.thumbstage.hydrogen.view.browse.atme.AtMeFragment;
import com.thumbstage.hydrogen.view.browse.mine.IAttendedClosedFragment;
import com.thumbstage.hydrogen.view.browse.mine.IAttendedOpenedFragment;
import com.thumbstage.hydrogen.view.browse.mine.IStartedClosedFragment;
import com.thumbstage.hydrogen.view.browse.mine.IStartedOpenedFragment;
import com.thumbstage.hydrogen.view.browse.published.PublishedClosedFragment;
import com.thumbstage.hydrogen.view.browse.published.PublishedOpenedFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract PublishedOpenedFragment contributePublishedOpenedFragment();
    @ContributesAndroidInjector
    abstract PublishedClosedFragment contributePublishedClosedFragment();
    @ContributesAndroidInjector
    abstract IStartedOpenedFragment contributeIStartedOpenedFragment();
    @ContributesAndroidInjector
    abstract IStartedClosedFragment contributeIStartedClosedFragment();
    @ContributesAndroidInjector
    abstract IAttendedOpenedFragment contributeIAttendedOpenedFragment();
    @ContributesAndroidInjector
    abstract IAttendedClosedFragment contributeIAttendedClosedFragment();
    @ContributesAndroidInjector
    abstract AtMeFragment contributeAtMeFragment();
}
