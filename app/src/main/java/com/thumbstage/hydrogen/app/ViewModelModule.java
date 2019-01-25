package com.thumbstage.hydrogen.app;

import android.arch.lifecycle.ViewModelProvider;

import com.thumbstage.hydrogen.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;

/**
 * Created by mike on 2018/3/10.
 */

@Module
abstract class ViewModelModule {

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}

