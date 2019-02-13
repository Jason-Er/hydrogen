package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Topic implements Parcelable {

    String name;
    String brief;
    String sponsor_name;
    String setting_url;
    String conversation_id;
    String started_by;

    public Topic() {}

    protected Topic(Parcel in) {
        name = in.readString();
        brief = in.readString();
        sponsor_name = in.readString();
        setting_url = in.readString();
        conversation_id = in.readString();
        started_by = in.readString();
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
        dest.writeString(conversation_id);
        dest.writeString(started_by);
    }

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

    public Topic setConversation_id(String conversation_id) {
        this.conversation_id = conversation_id;
        return this;
    }

    public Topic setStarted_by(String started_by) {
        this.started_by = started_by;
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

    public String getConversation_id() {
        return conversation_id;
    }

    public String getStarted_by() {
        return started_by;
    }
    // endregion
}
