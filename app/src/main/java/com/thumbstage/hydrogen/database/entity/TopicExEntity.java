package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "topic_ex",
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
    @ColumnInfo(name = "topic_id")
    protected final String topicId;
    @ColumnInfo(name = "mic_id")
    protected final String micId;
    protected final String type;

    public TopicExEntity(String topicId, String micId, String type) {
        this.topicId = topicId;
        this.micId = micId;
        this.type = type;
    }
}
