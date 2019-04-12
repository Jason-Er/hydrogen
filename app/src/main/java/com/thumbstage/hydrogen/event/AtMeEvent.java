package com.thumbstage.hydrogen.event;

public class AtMeEvent extends BaseEvent {
    public AtMeEvent(Object data) {
        super(data);
    }

    public AtMeEvent(Object data, String message) {
        super(data, message);
    }
}
