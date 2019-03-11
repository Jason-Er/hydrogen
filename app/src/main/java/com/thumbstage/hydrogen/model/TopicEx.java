package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TopicEx implements Parcelable {

    Topic topic;
    Mic mic;

    public TopicEx(Topic topic, Mic mic) {
        this.topic = topic;
        this.mic = mic;
    }

    protected TopicEx(Parcel in) {
        topic = in.readParcelable(Topic.class.getClassLoader());
        mic = in.readParcelable(Mic.class.getClassLoader());
    }

    public static final Creator<TopicEx> CREATOR = new Creator<TopicEx>() {
        @Override
        public TopicEx createFromParcel(Parcel in) {
            return new TopicEx(in);
        }

        @Override
        public TopicEx[] newArray(int size) {
            return new TopicEx[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(topic, flags);
        dest.writeParcelable(mic, flags);
    }

    // region getter and setter
    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Mic getMic() {
        return mic;
    }

    public void setMic(Mic mic) {
        this.mic = mic;
    }
    // endregion
}
