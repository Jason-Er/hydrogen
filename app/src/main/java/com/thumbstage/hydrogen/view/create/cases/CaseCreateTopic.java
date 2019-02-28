package com.thumbstage.hydrogen.view.create.cases;

import android.util.Log;

import com.thumbstage.hydrogen.app.UserGlobal;
import com.thumbstage.hydrogen.data.LCRepository;
import com.thumbstage.hydrogen.model.Line;
import com.thumbstage.hydrogen.model.LineType;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;
import com.thumbstage.hydrogen.view.create.ICreateActivityFunction;

public class CaseCreateTopic extends CaseBase implements ICreateActivityFunction {

    final String TAG = "CaseCreateTopic";

    public CaseCreateTopic() {
        Topic topic = new Topic();
    }

    @Override
    public void handleBottomBarEvent(ConversationBottomBarEvent event) {
        handleEvent(event);
    }

    protected void handleEvent(ConversationBottomBarEvent event) {
        switch (event.getMessage()) {
            case "text":
                addLine2Adapter((Line) event.getData());
                break;
            case "voice":

                break;
        }
    }

    // region implements interface ICreateActivityFunction
    @Override
    public void onActionOK() {
        Log.i(TAG, "onActionOK");
        if( topic.getMembers().size() == 0 ) {
            topic.getMembers().add(UserGlobal.getInstance().getCurrentUserId());
        }
        LCRepository.saveIStartedOpenedTopic(topic);
    }

    @Override
    public void onActionPublish() {
        Log.i(TAG, "onActionPublish");
        if( topic.getMembers().size() == 0 ) {
            topic.getMembers().add(UserGlobal.getInstance().getCurrentUserId());
        }
        LCRepository.savePublishedOpenedTopic(topic, new LCRepository.ICallBack() {
            @Override
            public void callback(String objectID) {

            }
        });
    }
    // endregion
}
