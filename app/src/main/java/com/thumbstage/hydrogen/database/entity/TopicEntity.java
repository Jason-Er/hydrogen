package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "topic",
        foreignKeys = {
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "started_by")})
public class TopicEntity {

    @PrimaryKey
    @NonNull
    protected String id;
    protected String name;
    protected String brief;

    @ColumnInfo(name = "setting_url")
    protected String setting_url;

    @ColumnInfo(name = "derive_from")
    protected String derive_from;

    @ColumnInfo(name = "started_by")
    protected String started_by;

    @ColumnInfo(name = "last_refresh")
    protected Date lastRefresh;

    // --- CONSTRUCTORS ---

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getSetting_url() {
        return setting_url;
    }

    public void setSetting_url(String setting_url) {
        this.setting_url = setting_url;
    }

    public String getDerive_from() {
        return derive_from;
    }

    public void setDerive_from(String derive_from) {
        this.derive_from = derive_from;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public String getStarted_by() {
        return started_by;
    }

    public void setStarted_by(String started_by) {
        this.started_by = started_by;
    }
}
