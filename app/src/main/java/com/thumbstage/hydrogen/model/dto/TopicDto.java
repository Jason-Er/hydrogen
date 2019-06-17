package com.thumbstage.hydrogen.model.dto;

import com.thumbstage.hydrogen.model.bo.CanOnTopic;
import com.thumbstage.hydrogen.model.bo.TopicTag;
import com.thumbstage.hydrogen.model.vo.Line;
import com.thumbstage.hydrogen.model.vo.Setting;
import com.thumbstage.hydrogen.model.vo.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TopicDto {

    String id;
    String name;
    String brief;
    SettingDto setting;
    UserDto sponsor;
    String derive_from; // topic id
    List<LineDto> dialogue;
    List<String> members;
    Set<TopicTag> tags;
    Map<String, Set<CanOnTopic>> userCan; // current user can
    Date updateAt;
    boolean isFinished;

    public TopicDto() {
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

    public SettingDto getSetting() {
        return setting;
    }

    public void setSetting(SettingDto setting) {
        this.setting = setting;
    }

    public UserDto getSponsor() {
        return sponsor;
    }

    public void setSponsor(UserDto sponsor) {
        this.sponsor = sponsor;
    }

    public List<LineDto> getDialogue() {
        return dialogue;
    }

    public void setDialogue(List<LineDto> dialogue) {
        this.dialogue = dialogue;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
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

}
