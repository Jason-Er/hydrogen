package com.thumbstage.hydrogen.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class MicTopic implements Parcelable {
    String micId;
    String topicId;

    public MicTopic(String micId, String topicId) {
        this.micId = micId;
        this.topicId = topicId;
    }

    protected MicTopic(Parcel in) {
        micId = in.readString();
        topicId = in.readString();
    }

    public static final Creator<MicTopic> CREATOR = new Creator<MicTopic>() {
        @Override
        public MicTopic createFromParcel(Parcel in) {
            return new MicTopic(in);
        }

        @Override
        public MicTopic[] newArray(int size) {
            return new MicTopic[size];
        }
    };

    public String getMicId() {
        return micId;
    }

    public void setMicId(String micId) {
        this.micId = micId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(micId);
        dest.writeString(topicId);
    }
}
