package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "topic_user_can",
        primaryKeys = { "topic_id", "user_id", "can" },
        indices = {@Index("topic_id"), @Index("user_id")},
        foreignKeys = {
                @ForeignKey(entity = TopicEntity.class,
                        parentColumns = "id",
                        childColumns = "topic_id",
                        onDelete = CASCADE),
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "user_id")
        })
public class TopicUserCanEntity {

    @NonNull
    @ColumnInfo(name = "topic_id")
    private String topicId;
    @NonNull
    @ColumnInfo(name = "user_id")
    private String userId;
    @NonNull
    private String can;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getCan() {
        return can;
    }

    public void setCan(@NonNull String can) {
        this.can = can;
    }
}
