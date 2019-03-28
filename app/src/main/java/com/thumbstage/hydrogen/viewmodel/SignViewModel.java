package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.repository.UserRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SignViewModel extends ViewModel {

    private UserRepository userRepository;

    @Inject
    public SignViewModel(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LiveData<User> signIn(String id, String password) {
        return userRepository.signIn(id, password);
    }
}
