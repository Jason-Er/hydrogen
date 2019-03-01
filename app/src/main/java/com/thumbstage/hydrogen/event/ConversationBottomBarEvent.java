package com.thumbstage.hydrogen.event;

import com.thumbstage.hydrogen.event.BaseEvent;

public class ConversationBottomBarEvent extends BaseEvent {
    public ConversationBottomBarEvent(String message) {
        super(message);
    }

    public ConversationBottomBarEvent(Object data) {
        super(data);
    }

    public ConversationBottomBarEvent(Object data, String message) {
        super(data, message);
    }
}
