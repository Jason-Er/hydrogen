package com.thumbstage.hydrogen.di.module;


import com.thumbstage.hydrogen.view.account.AccountFragment;
import com.thumbstage.hydrogen.view.account.ContactFragment;
import com.thumbstage.hydrogen.view.browse.atme.AtMeFragment;
import com.thumbstage.hydrogen.view.browse.mine.IAttendedClosedFragment;
import com.thumbstage.hydrogen.view.browse.mine.IAttendedOpenedFragment;
import com.thumbstage.hydrogen.view.browse.published.CommunityShowFragment;
import com.thumbstage.hydrogen.view.browse.published.CommunityTopicFragment;
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
    abstract CommunityTopicFragment contributePublishedOpenedFragment();
    @ContributesAndroidInjector
    abstract CommunityShowFragment contributePublishedClosedFragment();
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
