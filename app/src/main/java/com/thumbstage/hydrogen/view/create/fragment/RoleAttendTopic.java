package com.thumbstage.hydrogen.view.create.fragment;

import com.thumbstage.hydrogen.data.LCRepository;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;

public class RoleAttendTopic extends RoleBase {

    @Override
    public void handleBottomBarEvent(final ConversationBottomBarEvent event) {
        if(pipe == null) {
            LCRepository.copyPublishedOpenedTopic(topic, new LCRepository.ICallBack() {
                @Override
                public void callback(String objectID) {
                    pipe.setId(objectID);
                    // AVIMConversation conv = UserGlobal.getInstance().getConversation(objectID);
                    // imConversation = conv;
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
        pipe = null;
        appendTopicDialogue(topic);
        return this;
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
