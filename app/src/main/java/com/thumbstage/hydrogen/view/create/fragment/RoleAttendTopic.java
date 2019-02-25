package com.thumbstage.hydrogen.view.create.fragment;

import com.avos.avoscloud.im.v2.AVIMConversation;
import com.thumbstage.hydrogen.app.UserGlobal;
import com.thumbstage.hydrogen.data.LCRepository;
import com.thumbstage.hydrogen.model.Topic;
import com.thumbstage.hydrogen.event.ConversationBottomBarEvent;

public class RoleAttendTopic extends RoleBase {

    @Override
    public void handleBottomBarEvent(final ConversationBottomBarEvent event) {
        if(imConversation == null) {
            LCRepository.copyPublishedOpenedTopic(topic, new LCRepository.ICallBack() {
                @Override
                public void callback(String objectID) {
                    UserGlobal.getInstance().getConversation(objectID, new UserGlobal.ICallBack() {
                        @Override
                        public void callBack(AVIMConversation conv) {
                            imConversation = conv;
                            handleEvent(event);
                        }
                    });
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
