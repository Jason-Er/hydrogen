package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mic implements Parcelable {
    String id; // conversation id
    Topic topic;

    public Mic() {
        topic = new Topic();
    }

    protected Mic(Parcel in) {
        id = in.readString();
    }

    public static final Creator<Mic> CREATOR = new Creator<Mic>() {
        @Override
        public Mic createFromParcel(Parcel in) {
            return new Mic(in);
        }

        @Override
        public Mic[] newArray(int size) {
            return new Mic[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
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
    // endregion
}
