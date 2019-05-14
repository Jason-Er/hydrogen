package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "last_im_message",
        primaryKeys = {"mic_id", "me"},
        indices = {@Index("mic_id"), @Index("who"), @Index("me")},
        foreignKeys = {
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "me")})
public class IMMessageEntity {
    @NonNull
    private String me;
    @NonNull
    @ColumnInfo(name = "mic_id")
    private String micId;
    @NonNull
    private String who;
    @NonNull
    private Date when;
    private String what;
    private String type;

    @ColumnInfo(name = "is_browsed")
    private Boolean isBrowsed = false;
    @ColumnInfo(name = "last_refresh")
    private Date lastRefresh;

    @NonNull
    public String getMe() {
        return me;
    }

    public void setMe(@NonNull String me) {
        this.me = me;
    }

    @NonNull
    public String getMicId() {
        return micId;
    }

    public void setMicId(@NonNull String micId) {
        this.micId = micId;
    }

    @NonNull
    public String getWho() {
        return who;
    }

    public void setWho(@NonNull String who) {
        this.who = who;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    @NonNull
    public Date getWhen() {
        return when;
    }

    public void setWhen(@NonNull Date when) {
        this.when = when;
    }

    public Boolean getBrowsed() {
        return isBrowsed;
    }

    public void setBrowsed(Boolean browsed) {
        isBrowsed = browsed;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
