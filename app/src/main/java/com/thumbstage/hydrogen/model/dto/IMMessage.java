package com.thumbstage.hydrogen.model.dto;

import com.thumbstage.hydrogen.model.bo.LineType;

import java.util.Date;

public class IMMessage {
    String whoId;
    String what;
    Date when;
    LineType lineType;
    String micId;

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

    public String getMicId() {
        return micId;
    }

    public void setMicId(String micId) {
        this.micId = micId;
    }
}
