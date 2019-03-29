package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.utils.StringUtil;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    private final CloudAPI cloudAPI;
    private final ModelDB modelDB;
    private final Executor executor;

    private User defaultUser = new User(StringUtil.DEFAULT_USERID, StringUtil.DEFAULT_USERID, "");
    private User user = defaultUser;

    @Inject
    public UserRepository(CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        this.cloudAPI = cloudAPI;
        this.modelDB = modelDB;
        this.executor = executor;
        userLiveData.setValue(user);
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

    public LiveData<User> signUp(String name, String password, String email) {
        if( StringUtil.isValidMail(email) ) {
            cloudAPI.signUp(name, password, email, new CloudAPI.IReturnUser() {
                @Override
                public void callback(User user) {
                    userLiveData.setValue(user);
                }
            });
        }
        return userLiveData;
    }

    public LiveData<User> getCurrentUser() {
        User user = cloudAPI.getCurrentUser();
        if(user!=null) {
            userLiveData.setValue(user);
        }
        return userLiveData;
    }

    public void signOut() {
        cloudAPI.signOut();
        userLiveData.setValue(defaultUser);
    }
}
