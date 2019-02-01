package com.thumbstage.hydrogen.view.common;

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
