package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Topic implements Parcelable {

    String name;
    String brief;
    String sponsor_name;
    String setting_url;
    String started_by;
    List<Line> dialogue;
    List<String> members;

    public Topic() {}

    protected Topic(Parcel in) {
        name = in.readString();
        brief = in.readString();
        sponsor_name = in.readString();
        setting_url = in.readString();
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
        dest.writeString(sponsor_name);
        dest.writeString(setting_url);
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

    public Topic setSponsor_name(String sponsor_name) {
        this.sponsor_name = sponsor_name;
        return this;
    }

    public Topic setSetting_url(String setting_url) {
        this.setting_url = setting_url;
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

    public String getSponsor_name() {
        return sponsor_name;
    }

    public String getSetting_url() {
        return setting_url;
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
