package com.thumbstage.hydrogen.di.module;


import com.thumbstage.hydrogen.view.browse.published.PublishedOpenedFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract PublishedOpenedFragment contributePublishedOpenedFragment();
}
