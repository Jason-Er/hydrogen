package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Pipe implements Parcelable {
    String id; // conversation id

    public Pipe(String id) {
        this.id = id;
    }

    protected Pipe(Parcel in) {
        id = in.readString();
    }

    public static final Creator<Pipe> CREATOR = new Creator<Pipe>() {
        @Override
        public Pipe createFromParcel(Parcel in) {
            return new Pipe(in);
        }

        @Override
        public Pipe[] newArray(int size) {
            return new Pipe[size];
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
    // endregion
}
