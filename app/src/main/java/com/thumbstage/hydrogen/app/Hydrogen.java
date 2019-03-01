package com.thumbstage.hydrogen.app;

import android.app.Application;

import com.avos.avoscloud.AVUser;
import com.thumbstage.hydrogen.im.IMService;

public class Hydrogen extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        IMService.getInstance().init(getApplicationContext());
        UserGlobal.getInstance().setAvUser(AVUser.getCurrentUser());
    }
}
