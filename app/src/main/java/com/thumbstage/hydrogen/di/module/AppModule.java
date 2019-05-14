package com.thumbstage.hydrogen.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thumbstage.hydrogen.database.HyDatabase;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(includes = ViewModelModule.class)
public class AppModule {

    // --- DATABASE INJECTION ---

    @Provides
    @Singleton
    HyDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application,
                HyDatabase.class, "HyDatabase-db")
                .build();
    }

    @Provides
    @Singleton
    Context provideContext(Application application) {
        return application.getApplicationContext();
    }

    // --- REPOSITORY INJECTION ---

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    // --- NETWORK INJECTION ---

    @Provides
    Gson provideGson() { return new GsonBuilder().create(); }

}
