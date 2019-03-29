package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Line implements Parcelable {
    protected User who;
    protected Date when;
    protected String what;
    protected LineType lineType;

    public Line() {
    }

    public Line(User who, Date when, String what, LineType lineType) {
        this.who = who;
        this.when = when;
        this.what = what;
        this.lineType = lineType;
    }

    protected Line(Parcel in) {
        who = in.readParcelable(User.class.getClassLoader());
        what = in.readString();
        lineType = LineType.valueOf(in.readString());
        when = new Date(in.readLong());
    }

    public static final Creator<Line> CREATOR = new Creator<Line>() {
        @Override
        public Line createFromParcel(Parcel in) {
            return new Line(in);
        }

        @Override
        public Line[] newArray(int size) {
            return new Line[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(who, flags);
        dest.writeString(what);
        dest.writeString(lineType.name());
        dest.writeLong(when.getTime());
    }

    public User getWho() {
        return who;
    }

    public void setWho(User who) {
        this.who = who;
    }

    public Date getWhen() {
        return when;
    }

    public String getWhat() {
        return what;
    }

    public LineType getLineType() {
        return lineType;
    }

}
