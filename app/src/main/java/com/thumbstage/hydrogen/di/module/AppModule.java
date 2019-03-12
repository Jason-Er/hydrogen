package com.thumbstage.hydrogen.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thumbstage.hydrogen.database.HyDatabase;
import com.thumbstage.hydrogen.database.dao.UserDao;
import com.thumbstage.hydrogen.repository.TopicExRepository;

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
                HyDatabase.class, "HyDatabase.db")
                .build();
    }

    @Provides
    @Singleton
    UserDao provideUserDao(HyDatabase database) { return database.userDao(); }

    @Provides
    @Singleton
    TopicExRepository provideTopicExRepository(UserDao userDao, Executor executor) {
        return new TopicExRepository(userDao, executor);
    }
    // --- REPOSITORY INJECTION ---

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    // --- NETWORK INJECTION ---

    private static String BASE_URL = "https://api.github.com/";

    @Provides
    Gson provideGson() { return new GsonBuilder().create(); }

}
