package com.thumbstage.hydrogen.im;

import android.content.Context;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

import java.util.List;

import cn.leancloud.chatkit.cache.LCIMConversationItemCache;
import cn.leancloud.chatkit.cache.LCIMProfileCache;

public class IMService {

    private final String APP_ID = "mz0Nlz1o64kqyukS7pyj4sRe-gzGzoHsz";
    private final String APP_KEY = "o5CboiXK6ONj59aq0lMPJGS3";
    private volatile static IMService userGlobal = null;
    private IMMessageHandler imMessageHandler;
    private IMConversationHandler imConversationHandler;

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
        imMessageHandler = new IMMessageHandler(context);
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, imMessageHandler);

        // 和 Conversation 相关的事件的 handler
        imConversationHandler = new IMConversationHandler();
        AVIMMessageManager.setConversationEventHandler(imConversationHandler);
        AVIMClient.setUnreadNotificationEnabled(true);


        if(AVUser.getCurrentUser() != null) {
            LCIMProfileCache.getInstance().initDB(AVOSCloud.applicationContext, AVUser.getCurrentUser().getObjectId());
            LCIMConversationItemCache.getInstance().initDB(AVOSCloud.applicationContext, AVUser.getCurrentUser().getObjectId(), new AVCallback() {
                @Override
                protected void internalDone0(Object o, AVException avException) {

                }
            });
        }
    }

    public IMMessageHandler getImMessageHandler() {
        return imMessageHandler;
    }

    public IMConversationHandler getImConversationHandler() {
        return imConversationHandler;
    }
}
