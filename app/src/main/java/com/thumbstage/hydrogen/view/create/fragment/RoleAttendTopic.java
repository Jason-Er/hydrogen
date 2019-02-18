package com.thumbstage.hydrogen.view.create.fragment;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMTypedMessage;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMAudioMessage;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;
import com.thumbstage.hydrogen.app.User;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.utils.DataConvertUtil;
import com.thumbstage.hydrogen.utils.StringUtil;
import com.thumbstage.hydrogen.view.common.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.view.common.ICallBack;

import java.util.List;
import java.util.Map;

public class RoleAttendTopic extends RoleBase {

    @Override
    public void handleBottomBarEvent(final ConversationBottomBarEvent event) {
        if(imConversation == null) {
            createConversation(topic, new ICallBack() {
                @Override
                public void doAfter() {
                    addTopicDialogue(imConversation.getConversationId(), topic.getDialogue());
                    handleEvent(event);
                }
            });
        } else {
            handleEvent(event);
        }
    }

    @Override
    public RoleBase setTopic(Topic topic) {
        this.topic = topic;
        imConversation = null;
        for(Line line : topic.getDialogue()) {
            AVIMTypedMessage m;
            if( StringUtil.isUrl(line.getWhat()) ) { // default is Audio
                AVFile file = new AVFile("music", line.getWhat(), null);
                m = new AVIMAudioMessage(file);
            } else { // is text
                m = new AVIMTextMessage();
                ((AVIMTextMessage)m).setText(line.getWhat());
            }
            m.setFrom(line.getWho());
            m.setTimestamp(line.getWhen().getTime());
            sendMessage(m);
        }
        return this;
    }

    protected void addTopicDialogue(String conversationId, List<Line> dialogue) {
        List<Map> data = DataConvertUtil.convert2AVObject(dialogue);
        AVObject conversation = AVObject.createWithoutData("_Conversation", conversationId);
        conversation.addAllUnique("dialogue", data);
        conversation.saveInBackground();
    }

    protected void createConversation(Topic topic, final ICallBack iCallBack) {
        User.getInstance().getClient().createConversation(topic.getMembers(), topic.getName(), null, new AVIMConversationCreatedCallback() {
            @Override
            public void done(AVIMConversation conversation, AVIMException e) {
                if(e == null) {
                    imConversation = conversation;
                    iCallBack.doAfter();
                }
            }
        });
    }

    protected void handleEvent(ConversationBottomBarEvent event) {
        switch (event.getMessage()) {
            case "text":
                sendText((String) event.getData());
                break;
            case "voice":

                break;
        }
    }

}
