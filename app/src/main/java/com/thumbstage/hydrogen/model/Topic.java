package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Topic implements Parcelable, Cloneable {

    String id;
    String name;
    String brief;
    TopicType type;
    Setting setting;
    User started_by;
    String derive_from; // topic id
    List<Line> dialogue;
    List<User> members;

    public Topic() {
        name = "";
        brief = "";
        derive_from = "";
        dialogue = new ArrayList<>();
        members = new ArrayList<>();
    }

    protected Topic(Parcel in) {
        id = in.readString();
        name = in.readString();
        brief = in.readString();
        derive_from = in.readString();
        type = TopicType.valueOf(in.readString());
        members = new ArrayList<>();
        in.readList(members, User.class.getClassLoader());
        setting = in.readParcelable(Setting.class.getClassLoader());
        started_by = in.readParcelable(User.class.getClassLoader());
        dialogue = new ArrayList<>();
        in.readList(dialogue, Line.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(brief);
        dest.writeString(derive_from);
        dest.writeString(type.name());
        dest.writeList(members);
        dest.writeParcelable(setting, flags);
        dest.writeParcelable(started_by, flags);
        dest.writeList(dialogue);
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    public static Topic Builder() {
        return new Topic();
    }

    // region setter
    public Topic setId(String id) {
        this.id = id;
        return this;
    }

    public Topic setName(String name) {
        this.name = name;
        return this;
    }

    public Topic setBrief(String brief) {
        this.brief = brief;
        return this;
    }

    public Topic setSetting(Setting setting) {
        this.setting = setting;
        return this;
    }

    public Topic setStarted_by(User started_by) {
        this.started_by = started_by;
        return this;
    }

    public Topic setDialogue(List<Line> dialogue) {
        this.dialogue = dialogue;
        return this;
    }

    public Topic setMembers(List<User> members) {
        this.members = members;
        return this;
    }

    public Topic setDerive_from(String derive_from) {
        this.derive_from = derive_from;
        return this;
    }

    public Topic setType(TopicType type) {
        this.type = type;
        return this;
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

    // endregion


    @Override
    public Object clone() {
        Topic topic = null;
        try{
            topic = (Topic) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return topic;
    }
}
