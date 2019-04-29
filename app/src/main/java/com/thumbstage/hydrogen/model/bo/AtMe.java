package com.thumbstage.hydrogen.model.bo;

import java.util.Date;

public class AtMe {
    Mic mic;
    User who;
    Date when;
    String what;
    User me;
    boolean isBrowsed;

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

    public User getMe() {
        return me;
    }

    public void setMe(User me) {
        this.me = me;
    }

    public boolean isBrowsed() {
        return isBrowsed;
    }

    public void setBrowsed(boolean browsed) {
        isBrowsed = browsed;
    }
}
