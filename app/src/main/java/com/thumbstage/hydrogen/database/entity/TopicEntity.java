package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "topic",
        indices = {@Index("sponsor")},
        foreignKeys = {
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "sponsor")})
public class TopicEntity {

    @PrimaryKey
    @NonNull
    private String id;
    private String name;
    private String brief;

    @ColumnInfo(name = "setting_url")
    private String setting_url;

    @ColumnInfo(name = "derive_from")
    private String derive_from;

    @ColumnInfo(name = "sponsor")
    private String sponsor;

    @ColumnInfo(name = "is_finished")
    private Boolean isFinished = false;

    @ColumnInfo(name = "update_at")
    private Date updateAt;

    @ColumnInfo(name = "last_refresh")
    private Date lastRefresh;

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

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public Boolean getFinished() {
        return isFinished;
    }

    public void setFinished(Boolean finished) {
        isFinished = finished;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

}
