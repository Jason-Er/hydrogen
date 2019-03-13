package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.TopicEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;

import java.util.Date;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TopicDao {

    @Insert(onConflict = REPLACE)
    void insert(TopicEntity topicEntity);

    @Update
    void update(TopicEntity... topicEntities);

    @Delete
    void delete(TopicEntity... topicEntities);

    @Query("SELECT * FROM topic WHERE id = :id")
    TopicEntity get(String id);

    @Query("SELECT * FROM topic WHERE id = :topicId AND last_refresh > :lastRefreshMax LIMIT 1")
    UserEntity hasTopic(String topicId, Date lastRefreshMax);
}