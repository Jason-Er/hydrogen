package com.thumbstage.hydrogen.view.create.fragment;

import android.support.v7.widget.LinearLayoutManager;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageOption;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.LineType;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.utils.DataConvertUtil;
import com.thumbstage.hydrogen.utils.LogUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class RoleBase implements ITopicFragmentFunction{

    AVIMConversation imConversation;
    Topic topic = new Topic();
    TopicRecyclerAdapter itemAdapter;
    LinearLayoutManager layoutManager;

    public RoleBase setImConversation(AVIMConversation imConversation) {
        this.imConversation = imConversation;
        return this;
    }

    public RoleBase setTopic(Topic topic) {
        this.topic = topic;
        return this;
    }

    public RoleBase setItemAdapter(TopicRecyclerAdapter itemAdapter) {
        this.itemAdapter = itemAdapter;
        return this;
    }

    public RoleBase setLayoutManager(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    protected void sendText(String content) {
        AVIMTextMessage message = new AVIMTextMessage();
        message.setText(content);
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put("type", LineType.LT_DIALOGUE.name());
        message.setAttrs(attributes);
        sendMessage(message);
    }

    protected void sendMessage(AVIMMessage message) {
        sendMessage(message, true);
    }

    protected void sendMessage(AVIMMessage message, boolean addToList) {
        if (addToList) {
            itemAdapter.addMessage(message);
        }
        itemAdapter.notifyDataSetChanged();
        scrollToBottom();
        if( imConversation != null) {
            AVIMMessageOption option = new AVIMMessageOption();
            option.setReceipt(true);
            imConversation.sendMessage(message, option, new AVIMConversationCallback() {
                @Override
                public void done(AVIMException e) {
                    itemAdapter.notifyDataSetChanged();
                    if (null != e) {
                        LogUtils.logException(e);
                    }
                }
            });
            addOneLine(imConversation.getConversationId(), DataConvertUtil.convert2Line(message));
        }
    }

    protected void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(itemAdapter.getItemCount() - 1, 0);
    }

    protected void addOneLine(String conversationId, Line line) {
        Map data = DataConvertUtil.convert2AVObject(line);
        AVObject conversation = AVObject.createWithoutData("_Conversation", conversationId);
        conversation.addUnique("dialogue", data);
        conversation.saveInBackground();
    }

    public void clearTopic() {
        topic = new Topic();
    }
}
