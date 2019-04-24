package com.thumbstage.hydrogen.api;

import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.AVIMTypedMessageHandler;
import com.thumbstage.hydrogen.database.ModelDB;

public class IMMessageHandler extends AVIMTypedMessageHandler<AVIMTypedMessage> {
    final String TAG = "IMMessageHandler";
    private Context context;
    private ModelDB modelDB;

    public IMMessageHandler(Context context, ModelDB modelDB) {
        this.context = context;
        this.modelDB = modelDB;
    }

    @Override
    public void onMessage(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        Log.i(TAG, "callBack");
        message.getFrom();
    }

    @Override
    public void onMessageReceipt(AVIMTypedMessage message, AVIMConversation conversation, AVIMClient client) {
        super.onMessageReceipt(message, conversation, client);
    }

    /*
    private void sendEvent(AVIMTypedMessage message, AVIMConversation conversation) {
        Log.i(TAG, "sendEvent");
        IMIMTypeMessageEvent event = new IMIMTypeMessageEvent();
        event.message = message;
        event.conversation = conversation;
    }
    */

    private void sendNotification(final AVIMTypedMessage message, final AVIMConversation conversation) {
        if (null != conversation && null != message) {
            /*
            final String notificationContent = message instanceof AVIMTextMessage ?
                    ((AVIMTextMessage) message).getText() : context.getString(R.string.lcim_unspport_message_type);
            LCIMProfileCache.getInstance().getCachedUser(message.getFrom(), new AVCallback<LCChatKitUser>() {
                @Override
                protected void internalDone0(LCChatKitUser userProfile, AVException e) {
                    if (e != null) {
                        LCIMLogUtils.logException(e);
                    } else if (null != userProfile) {
                        String title = userProfile.getName();
                        Intent intent = getIMNotificationIntent(conversation.getConversationId(), message.getFrom());
                        LCIMNotificationUtils.showNotification(context, title, notificationContent, null, intent);
                    }
                }
            });
            */
        }
    }

}
