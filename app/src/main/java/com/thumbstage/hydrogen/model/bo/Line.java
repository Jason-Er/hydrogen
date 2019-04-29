package com.thumbstage.hydrogen.model.bo;

import java.util.Date;

public class Line {
    protected User who;
    protected Date when;
    protected String what;
    protected LineType lineType;

    public Line() {
        lineType = LineType.LT_DIALOGUE;
    }

    public Line(User who, Date when, String what, LineType lineType) {
        this.who = who;
        this.when = when;
        this.what = what;
        this.lineType = lineType;
    }

    public User getWho() {
        return who;
    }

    public void setWho(User who) {
        this.who = who;
    }

    public Date getWhen() {
        return when;
    }

    public String getWhat() {
        return what;
    }

    public LineType getLineType() {
        return lineType;
    }

}
