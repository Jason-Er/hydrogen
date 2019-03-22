package com.thumbstage.hydrogen.model;

import java.util.Date;

public class AtMe {
    Mic mic;
    User who;
    Date when;
    String what;

    public User getWho() {
        return who;
    }

    public void setWho(User who) {
        this.who = who;
    }

    public Mic getMic() {
        return mic;
    }

    public void setMic(Mic mic) {
        this.mic = mic;
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
}
