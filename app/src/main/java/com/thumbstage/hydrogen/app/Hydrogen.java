package com.thumbstage.hydrogen.app;

import android.app.Application;
import android.util.Log;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;

import cn.leancloud.chatkit.LCChatKit;

public class Hydrogen extends Application {

    private final String APP_ID = "mz0Nlz1o64kqyukS7pyj4sRe-gzGzoHsz";
    private final String APP_KEY = "o5CboiXK6ONj59aq0lMPJGS3";

    @Override
    public void onCreate() {
        super.onCreate();

        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);
        /*
        AVOSCloud.initialize(this, APP_ID, APP_KEY);
        AVOSCloud.setDebugLogEnabled(true);
        */
        UserGlobal.getInstance().setAvUser(AVUser.getCurrentUser());
    }
}
