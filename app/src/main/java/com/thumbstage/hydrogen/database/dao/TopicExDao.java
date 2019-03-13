package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.TopicExEntity;

import java.util.List;

@Dao
public interface TopicExDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TopicExEntity topicExEntity);

    @Update
    void update(TopicExEntity... topicExEntities);

    @Delete
    void delete(TopicExEntity... topicExEntities);

    @Query("SELECT * FROM topic_ex WHERE type = :type")
    List<TopicExEntity> get(String type);
}
