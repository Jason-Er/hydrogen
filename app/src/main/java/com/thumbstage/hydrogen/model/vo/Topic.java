package com.thumbstage.hydrogen.model.vo;

import com.thumbstage.hydrogen.model.bo.TopicTag;

import java.util.ArrayList;
import java.util.List;

public class Topic implements Cloneable {

    String id;
    String name;
    String brief;
    Setting setting;
    User started_by;
    String derive_from; // topic id
    List<Line> dialogue;
    List<User> members;
    List<TopicTag> tags;
    boolean isFinished;

    public Topic() {
        name = "";
        brief = "";
        derive_from = "";
        tags = new ArrayList<>();
        dialogue = new ArrayList<>();
        members = new ArrayList<>();
    }

    // region setter
    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public void setStarted_by(User started_by) {
        this.started_by = started_by;
    }

    public void setDialogue(List<Line> dialogue) {
        this.dialogue = dialogue;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public void setDerive_from(String derive_from) {
        this.derive_from = derive_from;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public void setTags(List<TopicTag> tags) {
        this.tags = tags;
    }
    // endregion

    // region getter

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBrief() {
        return brief;
    }

    public Setting getSetting() {
        return setting;
    }

    public User getStarted_by() {
        return started_by;
    }

    public List<User> getMembers() {
        return members;
    }

    public List<Line> getDialogue() {
        return dialogue;
    }

    public String getDerive_from() {
        return derive_from;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public List<TopicTag> getTags() {
        return tags;
    }
    // endregion


    @Override
    public Object clone() {
        Topic topic = null;
        try{
            topic = (Topic) super.clone();
            topic.setDerive_from(topic.getId());
            topic.setId("");
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return topic;
    }
}
