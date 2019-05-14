package com.thumbstage.hydrogen.event;

public class TopicBottomBarEvent extends BaseEvent {
    public TopicBottomBarEvent(String message) {
        super(message);
    }

    public TopicBottomBarEvent(Object data) {
        super(data);
    }

    public TopicBottomBarEvent(Object data, String message) {
        super(data, message);
    }
}
