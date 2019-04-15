package com.thumbstage.hydrogen.event;

public class PlayerControlEvent {
    private String message;
    float seekProcess;

    public PlayerControlEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public float getSeekProcess() {
        return seekProcess;
    }

    public void setSeekProcess(float seekProcess) {
        this.seekProcess = seekProcess;
    }
}
