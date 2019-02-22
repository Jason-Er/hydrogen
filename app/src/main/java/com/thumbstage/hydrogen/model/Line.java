package com.thumbstage.hydrogen.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Line implements Parcelable {
    String who;
    Date when;
    String what;
    LineType lineType;

    public Line(String who, Date when, String what, LineType lineType) {
        this.who = who;
        this.when = when;
        this.what = what;
        this.lineType = lineType;
    }

    protected Line(Parcel in) {
        who = in.readString();
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
        dest.writeString(who);
        dest.writeString(what);
        dest.writeString(lineType.name());
        dest.writeLong(when.getTime());
    }


    // region getter
    public String getWho() {
        return who;
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
    // endregion
}
