package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Topic implements Parcelable {

    String name;
    String brief;
    String setting_id; // save in _File object ID
    String started_by;
    List<Line> dialogue;
    List<String> members;

    public Topic() {
        name = "";
        brief = "";
        setting_id = "5c629287303f390047c13726"; // default setting file id
        started_by = "";
        dialogue = new ArrayList<>();
        members = new ArrayList<>();
    }

    protected Topic(Parcel in) {
        name = in.readString();
        brief = in.readString();
        setting_id = in.readString();
        started_by = in.readString();
        members = in.createStringArrayList();
        dialogue = new ArrayList<>();
        in.readList(dialogue, Line.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(brief);
        dest.writeString(setting_id);
        dest.writeString(started_by);
        dest.writeStringList(members);
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
    public Topic setName(String name) {
        this.name = name;
        return this;
    }

    public Topic setBrief(String brief) {
        this.brief = brief;
        return this;
    }

    public Topic setSetting_id(String setting_id) {
        this.setting_id = setting_id;
        return this;
    }

    public Topic setStarted_by(String started_by) {
        this.started_by = started_by;
        return this;
    }

    public Topic setDialogue(List<Line> dialogue) {
        this.dialogue = dialogue;
        return this;
    }

    public Topic setMembers(List<String> members) {
        this.members = members;
        return this;
    }

    // endregion

    // region getter
    public String getName() {
        return name;
    }

    public String getBrief() {
        return brief;
    }

    public String getSetting_id() {
        return setting_id;
    }

    public String getStarted_by() {
        return started_by;
    }

    public List<String> getMembers() {
        return members;
    }

    public List<Line> getDialogue() {
        return dialogue;
    }
    // endregion
}
