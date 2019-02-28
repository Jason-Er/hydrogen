package com.thumbstage.hydrogen.view.create.cases;

import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;

public class CaseContinueTopic extends CaseBase {

    @Override
    public void handleBottomBarEvent(ConversationBottomBarEvent event) {
        handleEvent(event);
    }

    protected void handleEvent(ConversationBottomBarEvent event) {
        switch (event.getMessage()) {
            case "text":
                addLine((Line) event.getData());
                break;
            case "voice":

                break;
        }
    }

    @Override
    public CaseBase setTopic(Topic topic) {
        this.topic = topic;
        if(topic != null) {
            addLines2Adapter(topic.getDialogue());
        } else {

        }
        return this;
    }
}
