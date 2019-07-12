package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "comment",
        indices = {@Index("sponsor")},
        foreignKeys = {
                @ForeignKey(entity = TopicEntity.class,
                        parentColumns = "id",
                        childColumns = "topic_id",
                        onDelete = CASCADE),
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "sponsor")})
public class CommentEntity {

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

    // --- CONSTRUCTORS ---

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

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

}
