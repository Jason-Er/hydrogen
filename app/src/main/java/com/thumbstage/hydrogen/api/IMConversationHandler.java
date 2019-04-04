package com.thumbstage.hydrogen.api;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.database.ModelDB;
import com.thumbstage.hydrogen.model.AtMe;
import com.thumbstage.hydrogen.model.Mic;
import com.thumbstage.hydrogen.model.User;
import com.thumbstage.hydrogen.model.callback.IReturnMic;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * Created by wli on 15/12/1.
 * 和 Conversation 相关的事件的 handler
 * 需要应用主动调用  AVIMMessageManager.setConversationEventHandler
 * 关于回调会何时执行可以参见 https://leancloud.cn/docs/realtime_guide-android.html#添加其他成员
 */
public class IMConversationHandler extends AVIMConversationEventHandler {

    String TAG = "IMConversationHandler";
    private ModelDB modelDB;
    private Executor executor;
    private CloudAPI cloudAPI;

    public IMConversationHandler(CloudAPI cloudAPI, ModelDB modelDB, Executor executor) {
        this.cloudAPI = cloudAPI;
        this.modelDB = modelDB;
        this.executor = executor;
    }

    @Override
    public void onUnreadMessagesCountUpdated(final AVIMClient client, final AVIMConversation conversation) {
        Log.i(TAG, "onUnreadMessagesCountUpdated ");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                cloudAPI.getMic(conversation.getConversationId(), new IReturnMic() {
                    @Override
                    public void callback(Mic mic) {
                        AVIMMessage lastMessage = conversation.getLastMessage();
                        final AtMe atMe = new AtMe();
                        atMe.setMic(mic);
                        if(lastMessage instanceof AVIMTextMessage)
                            atMe.setWhat(((AVIMTextMessage) lastMessage).getText());
                        for(User user: mic.getTopic().getMembers()) {
                            if(user.getId().equals(lastMessage.getFrom())) {
                                atMe.setWho(user);
                                break;
                            }
                        }
                        atMe.setWhen(new Date(lastMessage.getTimestamp()));
                        atMe.setMe(cloudAPI.getCurrentUser());
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                modelDB.saveAtMe(atMe);
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onLastDeliveredAtUpdated(AVIMClient client, AVIMConversation conversation) {
        Log.i(TAG, "onLastDeliveredAtUpdated ");
        /*
        LCIMConversationReadStatusEvent event = new LCIMConversationReadStatusEvent();
        event.conversationId = conversation.getConversationId();
        // EventBus.getDefault().post(event);
        */
    }

    @Override
    public void onLastReadAtUpdated(AVIMClient client, AVIMConversation conversation) {
        Log.i(TAG, "onLastReadAtUpdated ");
        /*
        LCIMConversationReadStatusEvent event = new LCIMConversationReadStatusEvent();
        event.conversationId = conversation.getConversationId();
        // EventBus.getDefault().post(event);
        */
    }

    @Override
    public void onMemberLeft(AVIMClient client, AVIMConversation conversation, List<String> members, String kickedBy) {
        // 因为不同用户需求不同，此处暂不做默认处理，如有需要，用户可以通过自定义 Handler 实现
    }

    @Override
    public void onMemberJoined(AVIMClient client, AVIMConversation conversation, List<String> members, String invitedBy) {
    }

    @Override
    public void onKicked(AVIMClient client, AVIMConversation conversation, String kickedBy) {
    }

    @Override
    public void onInvited(AVIMClient client, AVIMConversation conversation, String operator) {
    }

    @Override
    public void onMessageRecalled(AVIMClient client, AVIMConversation conversation, AVIMMessage message) {
        // EventBus.getDefault().post(new LCIMMessageUpdatedEvent(message));
    }

    @Override
    public void onMessageUpdated(AVIMClient client, AVIMConversation conversation, AVIMMessage message) {
        // EventBus.getDefault().post(new LCIMMessageUpdatedEvent(message));
    }

}
