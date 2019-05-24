package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.net.Uri;

import com.thumbstage.hydrogen.model.callback.IReturnBool;
import com.thumbstage.hydrogen.model.vo.User;
import com.thumbstage.hydrogen.repository.UserRepository;

import java.util.List;

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

    public User getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public void signOut() {
        userRepository.signOut();
    }

    public LiveData<List<User>> getContactByPageNum(int pageNum) {
        String userId = userRepository.getCurrentUser().getId();
        return userRepository.getContact(userId, pageNum);
    }

    public LiveData<List<User>> getUsers(List<String> userIds) {
        return userRepository.getUsers(userIds);
    }

    public LiveData<User> getUser(String userId) {
        return userRepository.getUser(userId);
    }

    public void addContact(User user) {
        userRepository.addContact(user);
    }

    public void updateUserAvatar(Uri imageUri, IReturnBool iReturnBool) {
        userRepository.updateUserAvatar(imageUri, iReturnBool);
    }
}
