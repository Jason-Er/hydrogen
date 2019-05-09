package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "topic_tag",
        primaryKeys = { "topic_id", "tag" },
        indices = {@Index("topic_id")},
        foreignKeys = {
                @ForeignKey(entity = TopicEntity.class,
                        parentColumns = "id",
                        childColumns = "topic_id",
                        onDelete = CASCADE)
        })
public class TopicTagEntity {
    @NonNull
    @ColumnInfo(name = "topic_id")
    private String topicId;
    @NonNull
    private String tag;

    @NonNull
    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(@NonNull String topicId) {
        this.topicId = topicId;
    }

    @NonNull
    public String getTag() {
        return tag;
    }

    public void setTag(@NonNull String tag) {
        this.tag = tag;
    }
}
