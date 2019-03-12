package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.OffsetDateTime;
import java.util.Date;

/**
 * Created by Philippe on 02/03/2018.
 */

@Entity
public class LineEntity {

    @PrimaryKey
    @NonNull
    @SerializedName("who")
    @Expose
    private String who;

    @Expose
    private Date when;

    @SerializedName("what")
    @Expose
    private String what;

    @SerializedName("line_type")
    @Expose
    private String line_type;

    private String topic;

    private Date lastRefresh;

    // --- CONSTRUCTORS ---

    public LineEntity() { }

    @Ignore
    public LineEntity(@NonNull String who, Date when, String what, String line_type,  Date lastRefresh) {
        this.who = who;
        this.when = when;
        this.what = what;
        this.line_type = line_type;
        this.lastRefresh = lastRefresh;
    }

    @NonNull
    public String getWho() {
        return who;
    }

    public void setWho(@NonNull String who) {
        this.who = who;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
        this.when = when;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getLine_type() {
        return line_type;
    }

    public void setLine_type(String line_type) {
        this.line_type = line_type;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
}
