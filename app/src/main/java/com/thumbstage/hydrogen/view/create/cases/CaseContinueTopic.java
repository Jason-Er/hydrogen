package com.thumbstage.hydrogen.view.create.cases;

import com.thumbstage.hydrogen.model.Line;
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

}
