package com.thumbstage.hydrogen.api;

import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.event.IMMessageEvent;
import com.thumbstage.hydrogen.model.bo.LineType;
import com.thumbstage.hydrogen.model.dto.IMMessage;
import com.thumbstage.hydrogen.model.dto.MicTopic;
import com.thumbstage.hydrogen.repository.FieldName;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

public class IMMessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {
    final String TAG = "IMMessageHandler";

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        Log.i(TAG, "callBack");
        IMMessage imMessage = new IMMessage();
        imMessage.setMicTopic(new MicTopic(conversation.getConversationId(), (String) conversation.get(FieldName.FIELD_TOPIC.name)));
        imMessage.setWhen(new Date(message.getTimestamp()));
        imMessage.setWhoId(message.getFrom());
        imMessage.setRead(false);
        if(message instanceof AVIMTextMessage) {
            imMessage.setWhat(((AVIMTextMessage) message).getText());
            LineType type = LineType.valueOf((String) ((AVIMTextMessage) message).getAttrs().get("type"));
            imMessage.setLineType(type);
        }
        IMMessageEvent event = new IMMessageEvent(imMessage, "onMessage");
        EventBus.getDefault().post(event);
    }

    @Override
    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }

}
