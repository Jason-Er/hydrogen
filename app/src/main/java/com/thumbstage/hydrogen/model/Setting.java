package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Setting extends HyFile implements Parcelable {

    public Setting(String id, String url, Boolean isInCloud) {
        super("", id, url, isInCloud);
    }

    protected Setting(Parcel in) {
        id = in.readString();
        url = in.readString();
        isInCloud = in.readByte() != 0;
    }

    public static final Creator<Setting> CREATOR = new Creator<Setting>() {
        @Override
        public Setting createFromParcel(Parcel in) {
            return new Setting(in);
        }

        @Override
        public Setting[] newArray(int size) {
            return new Setting[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(url);
        dest.writeByte((byte) (isInCloud? 1:0));
    }

}
