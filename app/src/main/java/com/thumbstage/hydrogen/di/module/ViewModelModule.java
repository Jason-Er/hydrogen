package com.thumbstage.hydrogen.di.module;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.thumbstage.hydrogen.di.key.ViewModelKey;
import com.thumbstage.hydrogen.viewmodel.AtMeViewModel;
import com.thumbstage.hydrogen.viewmodel.BrowseViewModel;
import com.thumbstage.hydrogen.viewmodel.FactoryViewModel;
import com.thumbstage.hydrogen.viewmodel.TopicViewModel;
import com.thumbstage.hydrogen.viewmodel.UserViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;


@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(BrowseViewModel.class)
    abstract ViewModel bindBrowseViewModel(BrowseViewModel browseViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TopicViewModel.class)
    abstract ViewModel bindTopicViewModel(TopicViewModel topicViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UserViewModel.class)
    abstract ViewModel bindUserViewModel(UserViewModel userViewModel);

    @Binds
    @IntoMap
    @ViewModelKey(AtMeViewModel.class)
    abstract ViewModel bindAtMeViewModel(AtMeViewModel atMeViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(FactoryViewModel factory);
}
