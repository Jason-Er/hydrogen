package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "mic",
        indices = {@Index("topic_id")},
        foreignKeys = {
                @ForeignKey(entity = TopicEntity.class,
                        parentColumns = "id",
                        childColumns = "topic_id")})
public class MicEntity {
    @PrimaryKey
    @NonNull
    private String id;

    @NonNull
    @ColumnInfo(name = "topic_id")
    private String topicId;

    @ColumnInfo(name = "update_at")
    private Date updateAt;
    @ColumnInfo(name = "last_refresh")
    private Date lastRefresh;
    @ColumnInfo(name = "has_new")
    private Boolean hasNew = false;


    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(@NonNull String topicId) {
        this.topicId = topicId;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }

    public Boolean getHasNew() {
        return hasNew;
    }

    public void setHasNew(Boolean hasNew) {
        this.hasNew = hasNew;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) {
            return true;
        }
        if(!(obj instanceof MicEntity)) {
            return false;
        }
        MicEntity entity = (MicEntity) obj;
        boolean status = lastRefresh.getTime() == entity.lastRefresh.getTime()
                && id.equals(entity.id);
        return status;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + (this.id != null ? this.id.hashCode() : 0);
        hash = 53 * hash + this.lastRefresh.hashCode();
        return hash;
    }
}
