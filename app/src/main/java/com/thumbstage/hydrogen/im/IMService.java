package com.thumbstage.hydrogen.im;

import android.content.Context;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

public class IMService {

    private final String APP_ID = "mz0Nlz1o64kqyukS7pyj4sRe-gzGzoHsz";
    private final String APP_KEY = "o5CboiXK6ONj59aq0lMPJGS3";
    private volatile static IMService userGlobal = null;

    public static IMService getInstance() {
        if (userGlobal == null) {
            synchronized (IMService.class) {
                if (userGlobal == null) {
                    userGlobal = new IMService();
                }
            }
        }
        return userGlobal;
    }

    private IMService() {

    }

    public void init(Context context) {
        AVOSCloud.initialize(context, APP_ID, APP_KEY);
        AVOSCloud.setDebugLogEnabled(true);
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, new IMMessageHandler(context));
    }

}
