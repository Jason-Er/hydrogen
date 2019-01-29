package com.thumbstage.hydrogen.view.common;

public class BaseEvent {

    private Object data;
    private String message;

    public BaseEvent(String message) {
        this.message = message;
    }

    public BaseEvent(Object data) {
        this.data = data;
    }

    public BaseEvent(Object data, String message) {
        this.data = data;
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

}
