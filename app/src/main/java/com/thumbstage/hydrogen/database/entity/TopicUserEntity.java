package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "topic_user",
        primaryKeys = { "topic_id", "user_id" },
        foreignKeys = {
                @ForeignKey(entity = TopicEntity.class,
                        parentColumns = "id",
                        childColumns = "topic_id",
                        onDelete = CASCADE),
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "user_id")
        })
public class TopicUserEntity {

    @ColumnInfo(name = "topic_id")
    protected final String topicId;
    @ColumnInfo(name = "user_id")
    protected final String userId;

    public TopicUserEntity(String topicId, String userId) {
            this.topicId = topicId;
            this.userId = userId;
    }

}
