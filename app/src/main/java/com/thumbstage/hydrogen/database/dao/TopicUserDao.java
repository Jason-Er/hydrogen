package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.TopicUserEntity;

import java.util.List;

@Dao
public interface TopicUserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TopicUserEntity topicUserEntity);

    @Update
    void update(TopicUserEntity... topicUserEntities);

    @Delete
    void delete(TopicUserEntity... topicUserEntities);

    @Query("SELECT * FROM topic_user WHERE topic_id = :topicId")
    List<TopicUserEntity> get(String topicId);
}
