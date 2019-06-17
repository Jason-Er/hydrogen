package com.thumbstage.hydrogen.model.dto;

import com.thumbstage.hydrogen.model.bo.LineType;
import com.thumbstage.hydrogen.model.bo.MessageType;
import com.thumbstage.hydrogen.model.vo.User;

import java.util.Date;

public class LineDto {
    protected String who;
    protected Date when;
    protected String what;
    protected LineType lineType;
    protected MessageType messageType;

    public LineDto() {
        lineType = LineType.LT_DIALOGUE;
    }

    public LineDto(String who, Date when, String what, LineType lineType) {
        this.who = who;
        this.when = when;
        this.what = what;
        this.lineType = lineType;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public LineType getLineType() {
        return lineType;
    }
    
    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }
}
