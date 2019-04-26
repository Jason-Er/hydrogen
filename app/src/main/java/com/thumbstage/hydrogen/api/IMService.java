package com.thumbstage.hydrogen.api;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class IMService {

    private IMMessageHandler imMessageHandler;
    private IMConversationHandler imConversationHandler;

    @Inject
    public IMService() {
        imMessageHandler = new IMMessageHandler();
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
