package com.thumbstage.hydrogen.view.create.fragment;

import android.support.v7.widget.LinearLayoutManager;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.utils.DataConvertUtil;

import java.util.Map;

public abstract class RoleBase implements ITopicFragmentFunction{

    AVIMConversation imConversation;
    Topic topic;
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

    protected void scrollToBottom() {
        layoutManager.scrollToPositionWithOffset(itemAdapter.getItemCount() - 1, 0);
    }

    protected void addOneLine(String conversationId, Line line) {
        Map data = DataConvertUtil.convert2AVObject(line);
        AVObject conversation = AVObject.createWithoutData("_Conversation", conversationId);
        conversation.addUnique("dialogue", data);
        conversation.saveInBackground();
    }
}
