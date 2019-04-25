package com.thumbstage.hydrogen.api;

import android.content.Context;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.thumbstage.hydrogen.database.ModelDB;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IMService {

    private IMMessageHandler imMessageHandler;
    private IMConversationHandler imConversationHandler;

    @Inject
    public IMService(Context context, CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        imMessageHandler = new IMMessageHandler();
        AVIMMessageManager.registerMessageHandler(AVIMTypedMessage.class, imMessageHandler);

        // 和 Conversation 相关的事件的 handler
        imConversationHandler = new IMConversationHandler(cloudAPI, modelDB, executor);
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
