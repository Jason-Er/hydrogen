package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.TopicEntity;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TopicDao {

    @Insert(onConflict = REPLACE)
    long insert(TopicEntity topicEntity);

    @Insert(onConflict = REPLACE)
    void insert(List<TopicEntity> topicEntities);

    @Update
    void update(TopicEntity... topicEntities);

    @Delete
    void delete(TopicEntity... topicEntities);

    @Query("SELECT * FROM topic WHERE id = :id")
    TopicEntity get(String id);

    @Query("SELECT * FROM topic WHERE id IN (:ids)")
    List<TopicEntity> get(List<String> ids);

    @Query("SELECT * FROM topic WHERE id IN (SELECT topic_id FROM topic_tag WHERE tag =:tag) AND is_finished =:isFinished AND last_refresh > :lastRefreshMax LIMIT 1")
    TopicEntity hasTopic(String tag, boolean isFinished, Date lastRefreshMax);

    @Query("SELECT * FROM topic WHERE id IN (SELECT topic_id FROM topic_tag WHERE tag =:tag) AND sponsor =:sponsor AND is_finished =:isFinished AND last_refresh > :lastRefreshMax LIMIT 1")
    TopicEntity hasTopic(String tag, String sponsor, boolean isFinished, Date lastRefreshMax);

}