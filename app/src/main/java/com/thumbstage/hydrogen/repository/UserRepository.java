package com.thumbstage.hydrogen.repository;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.util.Log;

import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.User;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    private final CloudAPI cloudAPI;
    private final ModelDB modelDB;
    private final Executor executor;

    @Inject
    public UserRepository(CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        this.cloudAPI = cloudAPI;
        this.modelDB = modelDB;
        this.executor = executor;
    }

    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public LiveData<User> signIn(String id, String password) {
        cloudAPI.signIn(id, password, new CloudAPI.IReturnUser() {
            @Override
            public void callback(User user) {
                userLiveData.setValue(user);
            }
        });
        return userLiveData;
    }
}
