package com.thumbstage.hydrogen.api;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.event.IMMicEvent;
import com.thumbstage.hydrogen.model.bo.LineType;
import com.thumbstage.hydrogen.model.dto.IMMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;
import java.util.List;

public class IMConversationHandler extends AVIMConversationEventHandler {

    String TAG = "IMConversationHandler";

    @Override
    public void onUnreadMessagesCountUpdated(final AVIMClient client, final AVIMConversation conversation) {
        Log.i(TAG, "onUnreadMessagesCountUpdated ");
        AVIMMessage message = conversation.getLastMessage();
        IMMessage imMessage = new IMMessage();
        imMessage.setWhen(new Date(message.getTimestamp()));
        imMessage.setWhoId(message.getFrom());
        if(message instanceof AVIMTextMessage) {
            imMessage.setWhat(((AVIMTextMessage) message).getText());
            LineType type = LineType.LT_DIALOGUE;
            try {
                 type = LineType.valueOf((String) ((AVIMTextMessage) message).getAttrs().get("type"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            imMessage.setLineType(type);
        }
        IMMicEvent event = new IMMicEvent(imMessage, "onUnreadMessage");
        EventBus.getDefault().post(event);
    }

    @Override
    public void onLastDeliveredAtUpdated(AVIMClient client, AVIMConversation conversation) {
        Log.i(TAG, "onLastDeliveredAtUpdated ");

    }

    @Override
    public void onLastReadAtUpdated(AVIMClient client, AVIMConversation conversation) {
        Log.i(TAG, "onLastReadAtUpdated ");

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
