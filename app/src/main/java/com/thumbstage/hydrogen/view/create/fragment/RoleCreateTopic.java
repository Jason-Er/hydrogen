package com.thumbstage.hydrogen.view.create.fragment;

import android.util.Log;

import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.LineType;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.utils.LCDBUtil;
import com.thumbstage.hydrogen.view.common.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.view.create.ICreateActivityFunction;

import java.util.Date;

public class RoleCreateTopic extends RoleBase implements ICreateActivityFunction {

    final String TAG = "RoleCreateTopic";

    public RoleCreateTopic() {
        Topic topic = new Topic();
    }

    @Override
    public void handleBottomBarEvent(ConversationBottomBarEvent event) {
        handleEvent(event);
    }

    protected void handleEvent(ConversationBottomBarEvent event) {
        switch (event.getMessage()) {
            case "text":
                sendText((String) event.getData());
                addDialogue((String) event.getData(), LineType.LT_DIALOGUE);
                break;
            case "voice":

                break;
        }
    }

    protected void addDialogue(String dialouge, LineType type) {
        topic.getDialogue().add(new Line(AVUser.getCurrentUser().getObjectId(),
                new Date(),
                dialouge,
                type));
    }

    protected void sendMessage(AVIMMessage message, boolean addToList) {
        if (addToList) {
            message.setFrom(AVUser.getCurrentUser().getObjectId());
            itemAdapter.addMessage(message);
        }
        itemAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    // region implements interface ICreateActivityFunction
    @Override
    public void onActionOK() {
        Log.i(TAG, "onActionOK");
        topic.getMembers().clear();
        topic.getMembers().add(AVUser.getCurrentUser().getObjectId());
        LCDBUtil.saveIStartedOpenedTopic(topic);
    }

    @Override
    public void onActionPublish() {
        Log.i(TAG, "onActionPublish");
        topic.getMembers().clear();
        topic.getMembers().add(AVUser.getCurrentUser().getObjectId());
        LCDBUtil.savePublishedOpenedTopic(topic);
    }
    // endregion
}
