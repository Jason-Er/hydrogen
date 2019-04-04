package com.thumbstage.hydrogen.api;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMConversationEventHandler;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.database.HyDatabase;
import com.thumbstage.hydrogen.database.entity.AtMeEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;
import com.thumbstage.hydrogen.repository.FieldName;
import com.thumbstage.hydrogen.repository.TableName;

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
    private HyDatabase database;
    private Executor executor;

    public IMConversationHandler(HyDatabase database, Executor executor) {
        this.database = database;
        this.executor = executor;
    }

    @Override
    public void onUnreadMessagesCountUpdated(AVIMClient client, final AVIMConversation conversation) {
        Log.i(TAG, "onUnreadMessagesCountUpdated ");
        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.runInTransaction(new Runnable() {
                    @Override
                    public void run() {
                        final AVIMMessage lastMessage = conversation.getLastMessage();
                        AVQuery<AVObject> avUser = new AVQuery<>(TableName.TABLE_USER.name);
                        avUser.getInBackground(lastMessage.getFrom(), new GetCallback<AVObject>() {
                            @Override
                            public void done(final AVObject who, AVException e) {
                                if(e == null) {
                                    executor.execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            UserEntity userEntity = new UserEntity();
                                            userEntity.setId(who.getObjectId());
                                            userEntity.setName((String)who.get(FieldName.FIELD_USERNAME.name));
                                            userEntity.setAvatar((String) who.get(FieldName.FIELD_AVATAR.name));
                                            userEntity.setLastRefresh(new Date());
                                            database.userDao().insert(userEntity);
                                            AtMeEntity atMeEntity = new AtMeEntity();
                                            atMeEntity.setWho(who.getObjectId());
                                            atMeEntity.setMicId(conversation.getConversationId());
                                            atMeEntity.setMe(AVUser.getCurrentUser().getObjectId());
                                            atMeEntity.setWhen(new Date(lastMessage.getTimestamp()));
                                            if(lastMessage instanceof AVIMTextMessage) {
                                                atMeEntity.setWhat(((AVIMTextMessage) lastMessage).getText());
                                            } else {
                                                // TODO: 4/4/2019 may audio so save url then
                                            }
                                            atMeEntity.setLastRefresh(new Date());
                                            database.atMeDao().insert(atMeEntity);
                                        }
                                    });
                                } else {
                                    return;
                                }
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
