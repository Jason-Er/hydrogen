package com.thumbstage.hydrogen.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.thumbstage.hydrogen.api.CloudAPI;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.callback.IReturnUser;
import com.thumbstage.hydrogen.utils.StringUtil;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserRepository {

    private final CloudAPI cloudAPI;
    private final ModelDB modelDB;
    private final Executor executor;

    private User defaultUser = new User(StringUtil.DEFAULT_USERID, StringUtil.DEFAULT_USERID, "");

    @Inject
    public UserRepository(CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        this.cloudAPI = cloudAPI;
        this.modelDB = modelDB;
        this.executor = executor;
        userLiveData.setValue(getCurrentUser());
    }

    private MutableLiveData<User> userLiveData = new MutableLiveData<>();

    public LiveData<User> signIn(final String id, final String password) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cloudAPI.signIn(id, password, new IReturnUser() {
                    @Override
                    public void callback(final User user) {
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                modelDB.saveUser(user);
                            }
                        });
                        userLiveData.setValue(user);
                    }
                });
            }
        });
        return userLiveData;
    }

    public LiveData<User> signUp(String name, String password, String email) {
        if( StringUtil.isValidMail(email) ) {
            cloudAPI.signUp(name, password, email, new IReturnUser() {
                @Override
                public void callback(User user) {
                    userLiveData.setValue(user);
                }
            });
        }
        return userLiveData;
    }

    public User getCurrentUser() {
        User user = cloudAPI.getCurrentUser();
        if( user == null) {
            user = defaultUser;
        }
        return user;
    }

    public void signOut() {
        cloudAPI.signOut();
        userLiveData.setValue(defaultUser);
    }

    public void addContact(final User user) {
        final User who = cloudAPI.getCurrentUser();
        if(who != null) {
            cloudAPI.addContact(who.getId(), user.getId(), new IReturnBool() {
                @Override
                public void callback(Boolean isOK) {
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            modelDB.saveContacts(who.getId(), Arrays.asList(user));
                        }
                    });
                }
            });
        }
    }

    public LiveData<List<User>> getContact(String userId, int pageNum) {
        refreshContact(userId, pageNum);
        return modelDB.getContact(userId, pageNum);
    }

    public LiveData<List<User>> getUsers(List<String> userIds) {
        return modelDB.getUsers(userIds);
    }

    private void refreshContact(final String userId, final int pageNum) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if( modelDB.isContactNeedFresh(userId) ) {
                    cloudAPI.getContact(userId, pageNum, new CloudAPI.IReturnUsers() {
                        @Override
                        public void callback(final List<User> users) {
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    modelDB.saveUserList(users);
                                    modelDB.saveContacts(userId, users);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

}
