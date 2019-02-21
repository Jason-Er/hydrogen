package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TopicEx implements Parcelable {

    Topic topic;
    Pipe pipe;

    public TopicEx(Topic topic, Pipe pipe) {
        this.topic = topic;
        this.pipe = pipe;
    }

    protected TopicEx(Parcel in) {
        topic = in.readParcelable(Topic.class.getClassLoader());
        pipe = in.readParcelable(Pipe.class.getClassLoader());
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
        dest.writeParcelable(pipe, flags);
    }

    // region getter and setter
    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Pipe getPipe() {
        return pipe;
    }

    public void setPipe(Pipe pipe) {
        this.pipe = pipe;
    }
    // endregion
}
