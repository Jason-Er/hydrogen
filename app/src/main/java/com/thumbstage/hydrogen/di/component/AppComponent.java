package com.thumbstage.hydrogen.di.component;

import android.app.Application;

import com.thumbstage.hydrogen.app.Hydrogen;
import com.thumbstage.hydrogen.di.module.ActivityModule;
import com.thumbstage.hydrogen.di.module.AppModule;
import com.thumbstage.hydrogen.di.module.FragmentModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;


@Singleton
@Component(modules={AndroidSupportInjectionModule.class, ActivityModule.class, FragmentModule.class, AppModule.class})
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);
        AppComponent build();
    }

    void inject(Hydrogen app);
}
