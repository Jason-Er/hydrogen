package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.TopicUserCanEntity;

import java.util.List;

@Dao
public interface TopicUserCanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TopicUserCanEntity topicUserEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TopicUserCanEntity> topicUserEntity);

    @Update
    void update(TopicUserCanEntity... topicUserEntities);

    @Delete
    void delete(TopicUserCanEntity... topicUserEntities);

    @Query("SELECT * FROM topic_user_can WHERE topic_id =:topicId")
    List<TopicUserCanEntity> get(String topicId);

    @Query("SELECT * FROM topic_user_can WHERE topic_id =:topicId AND user_id =:userId ")
    List<TopicUserCanEntity> get(String topicId, String userId);
}
