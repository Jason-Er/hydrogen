package com.thumbstage.hydrogen.event;

public class SelectContactUserEvent {
    String userId;
    boolean isChecked;
    public SelectContactUserEvent(String userId, boolean isChecked) {
        this.userId = userId;
        this.isChecked = isChecked;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
