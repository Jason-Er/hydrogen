package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.TopicExEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface TopicExDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TopicExEntity topicExEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TopicExEntity> topicExEntities);

    @Update
    void update(TopicExEntity... topicExEntities);

    @Delete
    void delete(TopicExEntity... topicExEntities);

    @Query("SELECT * FROM topic_ex WHERE type = :type ORDER BY last_refresh DESC LIMIT :perPageNum OFFSET :offset ")
    List<TopicExEntity> get(String type, int perPageNum, int offset);

    @Query("SELECT * FROM topic_ex, topic WHERE type = :type AND topic_id = topic.id AND topic.started_by =:started_by ORDER BY last_refresh DESC LIMIT :perPageNum OFFSET :offset ")
    List<TopicExEntity> get(String type, String started_by, int perPageNum, int offset);

    @Query("SELECT * FROM topic_ex, topic WHERE topic_id = topic.id and type = :type and topic.started_by = :started_by")
    List<TopicExEntity> get(String type, String started_by);

    @Query("SELECT * FROM topic_ex WHERE type = :type AND last_refresh > :lastRefreshMax LIMIT 1")
    TopicExEntity hasTopicEx(String type, Date lastRefreshMax);
}
