package com.thumbstage.hydrogen.database.entity;

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
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("name")
    @Expose
    private String name;

    private Date lastRefresh;

    // --- CONSTRUCTORS ---

    public UserEntity() { }

    public UserEntity(@NonNull String id, String login, String avatar_url, String name, String company, String blog, Date lastRefresh) {
        this.id = id;
        this.avatar = avatar_url;
        this.name = name;
        this.lastRefresh = lastRefresh;
    }

    // --- GETTER ---

    public String getId() { return id; }
    public String getAvatar() { return avatar; }
    public Date getLastRefresh() { return lastRefresh; }
    public String getName() { return name; }

    // --- SETTER ---

    public void setId(String id) { this.id = id; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setLastRefresh(Date lastRefresh) { this.lastRefresh = lastRefresh; }
    public void setName(String name) { this.name = name; }

}
