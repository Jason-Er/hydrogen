package com.thumbstage.hydrogen.view.common;

public class SignEvent extends BaseEvent {
    public SignEvent(String message) {
        super(message);
    }

    public SignEvent(Object data) {
        super(data);
    }

    public SignEvent(Object data, String message) {
        super(data, message);
    }
}
