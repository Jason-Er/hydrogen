package com.thumbstage.hydrogen.model.vo;

import com.thumbstage.hydrogen.model.bo.CanOnTopic;
import com.thumbstage.hydrogen.model.bo.TopicTag;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Topic implements Cloneable {

    String id;
    String name;
    String brief;
    String badgeUrl;
    Setting setting;
    User sponsor;
    String derive_from; // topic id
    List<Line> dialogue;
    List<User> members;
    Set<TopicTag> tags;
    Map<String, Set<CanOnTopic>> userCan; // current user can
    Date updateAt;
    boolean isFinished;

    public Topic() {
        name = "";
        brief = "";
        derive_from = "";
        tags = new LinkedHashSet<>();
        dialogue = new ArrayList<>();
        members = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public User getSponsor() {
        return sponsor;
    }

    public void setSponsor(User sponsor) {
        this.sponsor = sponsor;
    }

    public List<Line> getDialogue() {
        return dialogue;
    }

    public void setDialogue(List<Line> dialogue) {
        this.dialogue = dialogue;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }

    public String getDerive_from() {
        return derive_from;
    }

    public void setDerive_from(String derive_from) {
        this.derive_from = derive_from;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public Set<TopicTag> getTags() {
        return tags;
    }

    public void setTags(Set<TopicTag> tags) {
        this.tags = tags;
    }

    public Map<String, Set<CanOnTopic>> getUserCan() {
        return userCan;
    }

    public void setUserCan(Map<String, Set<CanOnTopic>> userCan) {
        this.userCan = userCan;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getBadgeUrl() {
        return badgeUrl;
    }

    public void setBadgeUrl(String badgeUrl) {
        this.badgeUrl = badgeUrl;
    }

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
