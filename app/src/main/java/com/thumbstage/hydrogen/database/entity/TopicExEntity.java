package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "topic_ex",
        indices = {@Index("topic_id"), @Index("mic_id")},
        foreignKeys = {
                @ForeignKey(entity = TopicEntity.class,
                        parentColumns = "id",
                        childColumns = "topic_id",
                        onDelete = CASCADE),
                @ForeignKey(entity = MicEntity.class,
                        parentColumns = "id",
                        childColumns = "mic_id")
        })
public class TopicExEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "topic_id")
    protected String topicId;
    @ColumnInfo(name = "mic_id")
    protected String micId;
    protected String type;

    @ColumnInfo(name = "last_refresh")
    protected Date lastRefresh;

    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getMicId() {
        return micId;
    }

    public void setMicId(String micId) {
        this.micId = micId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
}
