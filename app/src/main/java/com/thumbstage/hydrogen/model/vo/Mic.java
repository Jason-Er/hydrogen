package com.thumbstage.hydrogen.model.vo;

import java.util.Date;

public class Mic implements Cloneable {
    String id; // conversation id
    Topic topic;
    boolean hasNew;
    Date updateAt;
    Date lastRefresh;

    public Mic() {
        topic = new Topic();
    }

    // region getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public boolean isHasNew() {
        return hasNew;
    }

    public void setHasNew(boolean hasNew) {
        this.hasNew = hasNew;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
    // endregion

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Mic)) {
            return false;
        }
        Mic mic = (Mic) obj;
        boolean status = updateAt.compareTo(mic.updateAt) == 0 ? true : false
                && id.equals(mic.id);
        return status;
    }

    public boolean customEquals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof Mic)) {
            return false;
        }
        Mic mic = (Mic) obj;
        boolean status = lastRefresh.compareTo(mic.lastRefresh) == 0 ? true : false
                && id.equals(mic.id);
        return status;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 53 * hash + this.topic.hashCode();
        return hash;
    }

    @Override
    public Object clone(){
        Mic mic = null;
        try{
            mic = (Mic) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        mic.topic = (Topic) topic.clone();
        mic.setId("");
        return mic;
    }
}
