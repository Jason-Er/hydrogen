package com.thumbstage.hydrogen.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.avos.avoscloud.AVUser;

public class UserViewModel extends ViewModel {
    private AVUser avUser = null;

    public AVUser getAvUser() {
        return avUser;
    }

    public void setAvUser(AVUser avUser) {
        this.avUser = avUser;
    }
}
