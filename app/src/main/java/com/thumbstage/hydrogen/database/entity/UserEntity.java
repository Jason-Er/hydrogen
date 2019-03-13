package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

@Entity(tableName = "user")
public class UserEntity {

    @PrimaryKey
    @NonNull
    protected String id;
    protected String avatar;
    protected String name;

    @ColumnInfo(name = "last_refresh")
    protected Date lastRefresh;


    public String getId() { return id; }
    public String getAvatar() { return avatar; }
    public Date getLastRefresh() { return lastRefresh; }
    public String getName() { return name; }

    public void setId(String id) { this.id = id; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setLastRefresh(Date lastRefresh) { this.lastRefresh = lastRefresh; }
    public void setName(String name) { this.name = name; }

}
