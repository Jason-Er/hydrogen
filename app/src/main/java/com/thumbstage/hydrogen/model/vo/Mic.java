package com.thumbstage.hydrogen.model.vo;

import java.util.Date;

public class Mic implements Cloneable {
    String id; // conversation id
    Topic topic;
    boolean hasNew;
    Date updateAt;

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

    // endregion

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
