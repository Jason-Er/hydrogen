package com.thumbstage.hydrogen.di.module;


import com.thumbstage.hydrogen.view.account.AccountFragment;
import com.thumbstage.hydrogen.view.account.ContactFragment;
import com.thumbstage.hydrogen.view.browse.atme.AtMeFragment;
import com.thumbstage.hydrogen.view.browse.mine.IAttendedClosedFragment;
import com.thumbstage.hydrogen.view.browse.mine.IAttendedOpenedFragment;
import com.thumbstage.hydrogen.view.browse.mine.IPublishedClosedFragment;
import com.thumbstage.hydrogen.view.browse.mine.IPublishedOpenedFragment;
import com.thumbstage.hydrogen.view.browse.mine.IStartedClosedFragment;
import com.thumbstage.hydrogen.view.browse.mine.IStartedOpenedFragment;
import com.thumbstage.hydrogen.view.browse.published.PublishedClosedFragment;
import com.thumbstage.hydrogen.view.browse.published.PublishedOpenedFragment;
import com.thumbstage.hydrogen.view.common.BasicBrowseFragment;
import com.thumbstage.hydrogen.view.create.assist.TopicInfoFragment;
import com.thumbstage.hydrogen.view.create.assist.TopicMemberFragment;
import com.thumbstage.hydrogen.view.create.fragment.TopicFragment;
import com.thumbstage.hydrogen.view.show.fragment.ShowFragment;
import com.thumbstage.hydrogen.view.sign.SignInFragment;
import com.thumbstage.hydrogen.view.sign.SignUpFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {
    @ContributesAndroidInjector
    abstract BasicBrowseFragment contributeBasicBrowseFragment();
    @ContributesAndroidInjector
    abstract PublishedOpenedFragment contributePublishedOpenedFragment();
    @ContributesAndroidInjector
    abstract PublishedClosedFragment contributePublishedClosedFragment();
    @ContributesAndroidInjector
    abstract IPublishedOpenedFragment contributeIPublishedOpenedFragment();
    @ContributesAndroidInjector
    abstract IPublishedClosedFragment contributeIPublishedClosedFragment();
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
    @ContributesAndroidInjector
    abstract TopicFragment contributeTopicFragment();
    @ContributesAndroidInjector
    abstract ShowFragment contributeShowFragment();
    @ContributesAndroidInjector
    abstract SignInFragment contributeSignInFragment();
    @ContributesAndroidInjector
    abstract SignUpFragment contributeSignUpFragment();
    @ContributesAndroidInjector
    abstract AccountFragment contributeAccountFragment();
    @ContributesAndroidInjector
    abstract ContactFragment contributeContactFragment();
    @ContributesAndroidInjector
    abstract TopicMemberFragment contributeTopicMemberFragment();
    @ContributesAndroidInjector
    abstract TopicInfoFragment contributeTopicInfoFragment();

}
