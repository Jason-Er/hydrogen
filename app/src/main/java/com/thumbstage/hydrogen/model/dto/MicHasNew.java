package com.thumbstage.hydrogen.model.dto;

public class MicHasNew {
    String micId;
    boolean hasNew;

    public MicHasNew(String micId, boolean hasNew) {
        this.micId = micId;
        this.hasNew = hasNew;
    }

    public String getMicId() {
        return micId;
    }

    public void setMicId(String micId) {
        this.micId = micId;
    }

    public boolean isHasNew() {
        return hasNew;
    }

    public void setHasNew(boolean hasNew) {
        this.hasNew = hasNew;
    }
}
