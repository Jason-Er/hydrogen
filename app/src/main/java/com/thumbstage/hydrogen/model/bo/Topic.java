package com.thumbstage.hydrogen.model.bo;

import java.util.ArrayList;
import java.util.List;

public class Topic implements Cloneable {

    String id;
    String name;
    String brief;
    TopicType type;
    Setting setting;
    User started_by;
    String derive_from; // topic id
    List<Line> dialogue;
    List<User> members;
    boolean isFinished;

    public Topic() {
        name = "";
        brief = "";
        derive_from = "";
        type = TopicType.UNDEFINED;
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

    public void setType(TopicType type) {
        this.type = type;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
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

    public TopicType getType() {
        return type;
    }

    public boolean isFinished() {
        return isFinished;
    }
    // endregion


    @Override
    public Object clone() {
        Topic topic = null;
        try{
            topic = (Topic) super.clone();
            topic.setDerive_from(topic.getId());
            topic.setType(TopicType.UNDEFINED);
            topic.setId("");
            topic.setStarted_by(null);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return topic;
    }
}
