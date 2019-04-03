package com.thumbstage.hydrogen.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.database.HyDatabase;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.api.IMService;
import com.thumbstage.hydrogen.repository.TopicRepository;

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
    ModelDB provideModelEntityConvert(HyDatabase database) {
        return new ModelDB(database);
    }

    @Provides
    @Singleton
    CloudAPI provideCloudAPI(Application application) {
        return new CloudAPI(application.getApplicationContext());
    }

    @Provides
    @Singleton
    TopicRepository provideTopicExRepository(CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        return new TopicRepository(cloudAPI, modelDB, executor);
    }

    @Provides
    @Singleton
    IMService provideIMService(Application application, ModelDB modelDB) {
        return new IMService(application.getApplicationContext(), modelDB);
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
