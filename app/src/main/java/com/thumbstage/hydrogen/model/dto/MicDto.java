package com.thumbstage.hydrogen.model.dto;

import java.util.Date;

public class MicDto {
    String id; // conversation id
    TopicDto topic;
    boolean hasNew;
    Date updateAt;

    public MicDto() {
        topic = new TopicDto();
    }

    // region getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TopicDto getTopic() {
        return topic;
    }

    public void setTopic(TopicDto topic) {
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
}
