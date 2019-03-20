package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "at_me",
        primaryKeys = {"mic_id", "me"},
        indices = {@Index("mic_id"), @Index("who"), @Index("me")},
        foreignKeys = {
                @ForeignKey(entity = MicEntity.class,
                        parentColumns = "id",
                        childColumns = "mic_id"),
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "who"),
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "me")})
public class AtMeEntity {
    @NonNull
    protected String me;
    @NonNull
    @ColumnInfo(name = "mic_id")
    protected String micId;
    @NonNull
    protected String who;
    protected String what;
    @ColumnInfo(name = "last_refresh")
    protected Date lastRefresh;

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
}
