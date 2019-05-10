package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.TopicTagEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TopicTagDao {
    @Insert(onConflict = REPLACE)
    long insert(TopicTagEntity topicEntity);

    @Insert(onConflict = REPLACE)
    void insert(List<TopicTagEntity> topicEntities);

    @Update
    void update(TopicTagEntity... topicEntities);

    @Delete
    void delete(TopicTagEntity... topicEntities);

    @Query("SELECT * FROM topic_tag WHERE topic_id = :topicId")
    List<TopicTagEntity> get(String topicId);
}
