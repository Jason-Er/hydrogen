package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Setting implements Parcelable {
    String id;
    String url;

    public Setting(String id, String url) {
        this.id = id;
        this.url = url;
    }

    protected Setting(Parcel in) {
        id = in.readString();
        url = in.readString();
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
    }

    // region getter and setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    //endregion
}
