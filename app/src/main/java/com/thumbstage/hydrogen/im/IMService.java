package com.thumbstage.hydrogen.im;

import android.content.Context;

import com.avos.avoscloud.AVCallback;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.thumbstage.hydrogen.database.ModelDB;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IMService {

    private final String APP_ID = "mz0Nlz1o64kqyukS7pyj4sRe-gzGzoHsz";
    private final String APP_KEY = "o5CboiXK6ONj59aq0lMPJGS3";
    private volatile static IMService userGlobal = null;
    private IMMessageHandler imMessageHandler;
    private IMConversationHandler imConversationHandler;


    @Inject
    public IMService(Context context, ModelDB modelDB) {
        AVOSCloud.initialize(context, APP_ID, APP_KEY);
        AVOSCloud.setDebugLogEnabled(true);
        imMessageHandler = new IMMessageHandler(context, modelDB);
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, imMessageHandler);

        // 和 Conversation 相关的事件的 handler
        imConversationHandler = new IMConversationHandler();
        AVIMMessageManager.setConversationEventHandler(imConversationHandler);
        AVIMClient.setUnreadNotificationEnabled(true);
    }

    public IMMessageHandler getImMessageHandler() {
        return imMessageHandler;
    }

    public IMConversationHandler getImConversationHandler() {
        return imConversationHandler;
    }
}
