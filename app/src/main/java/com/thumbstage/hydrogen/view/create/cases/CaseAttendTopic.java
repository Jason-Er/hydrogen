package com.thumbstage.hydrogen.view.create.cases;

import android.text.TextUtils;

import com.thumbstage.hydrogen.data.LCRepository;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.Pipe;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;

public class CaseAttendTopic extends CaseBase {

    @Override
    public void handleBottomBarEvent(final ConversationBottomBarEvent event) {
        if(TextUtils.isEmpty(pipe.getId())) {
            LCRepository.copyPublishedOpenedTopic(topic, new LCRepository.IReturnPipe() {
                @Override
                public void callback(Pipe p) {
                    pipe = p;
                    handleEvent(event);
                }
            });
        } else {
            handleEvent(event);
        }
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
