package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.TopicUserEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;

import java.util.List;

@Dao
public interface TopicUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TopicUserEntity topicUserEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TopicUserEntity> topicUserEntity);

    @Update
    void update(TopicUserEntity... topicUserEntities);

    @Delete
    void delete(TopicUserEntity... topicUserEntities);

    @Query("SELECT * FROM user WHERE id IN (" +
            "SELECT user_id FROM topic_user WHERE topic_id =:topicId)")
    List<UserEntity> get(String topicId);
}
