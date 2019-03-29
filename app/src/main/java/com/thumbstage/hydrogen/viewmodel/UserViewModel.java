package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.repository.UserRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserViewModel extends ViewModel {

    private UserRepository userRepository;

    @Inject
    public UserViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<User> signUp(String name, String password, String email) {
        return userRepository.signUp(name, password, email);
    }

    public LiveData<User> signIn(String id, String password) {
        return userRepository.signIn(id, password);
    }

    public LiveData<User> getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public void signOut() {
        userRepository.signOut();
    }
}
