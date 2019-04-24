package com.thumbstage.hydrogen.event;

import com.thumbstage.hydrogen.model.bo.Line;

public class IMMessageEvent {
    Line line;
    String micId;

    public IMMessageEvent(Line line, String micId) {
        this.line = line;
        this.micId = micId;
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public String getMicId() {
        return micId;
    }

    public void setMicId(String micId) {
        this.micId = micId;
    }
}
