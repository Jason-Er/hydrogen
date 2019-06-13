package com.thumbstage.hydrogen.model.dto;

import com.thumbstage.hydrogen.model.bo.LineType;

import java.util.Date;

public class IMMessage {
    String whoId;
    String what;
    String meId;
    Date when;
    LineType lineType;
    MicTopic micTopic;
    boolean isRead;

    public String getWhoId() {
        return whoId;
    }

    public void setWhoId(String whoId) {
        this.whoId = whoId;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public LineType getLineType() {
        return lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public MicTopic getMicTopic() {
        return micTopic;
    }

    public void setMicTopic(MicTopic micTopic) {
        this.micTopic = micTopic;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getMeId() {
        return meId;
    }

    public void setMeId(String meId) {
        this.meId = meId;
    }
}
