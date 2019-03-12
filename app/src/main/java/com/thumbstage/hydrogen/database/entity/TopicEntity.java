package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by Philippe on 02/03/2018.
 */

@Entity
public class TopicEntity {

    @PrimaryKey
    @NonNull
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("brief")
    @Expose
    private String brief;

    @SerializedName("setting_url")
    @Expose
    private String setting_url;

    @SerializedName("derive_from")
    @Expose
    private String derive_from;

    private Date lastRefresh;

    // --- CONSTRUCTORS ---

    public TopicEntity() { }

    @Ignore
    public TopicEntity(@NonNull String id, String name, String brief, String setting_url, String derive_from, Date lastRefresh) {
        this.id = id;
        this.name = name;
        this.brief = brief;
        this.setting_url = setting_url;
        this.derive_from = derive_from;
        this.lastRefresh = lastRefresh;
    }

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
}
