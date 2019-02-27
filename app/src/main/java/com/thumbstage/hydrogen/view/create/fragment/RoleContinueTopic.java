package com.thumbstage.hydrogen.view.create.fragment;

import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;

public class RoleContinueTopic extends RoleBase {

    @Override
    public void handleBottomBarEvent(ConversationBottomBarEvent event) {
        handleEvent(event);
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

    @Override
    public RoleBase setTopic(Topic topic) {
        this.topic = topic;
        if(topic != null) {
            appendTopicDialogue(topic);
        } else {

        }
        return this;
    }
}
