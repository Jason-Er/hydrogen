package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mic implements Parcelable {
    String id; // conversation id
    String name;
    Topic topic;

    public Mic(String id) {
        this.id = id;
    }

    protected Mic(Parcel in) {
        id = in.readString();
        name = in.readString();

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
        dest.writeString(name);
    }

    // region getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    // endregion
}
