package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.LikeEntity;

import java.util.List;

@Dao
public interface LikeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LikeEntity likeEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<LikeEntity> likeEntities);

    @Update
    void update(LikeEntity... likeEntities);

    @Delete
    void delete(LikeEntity... likeEntities);

    @Query("SELECT * FROM 'like' WHERE user_id=:userId AND topic_id =:topicId")
    LikeEntity get(String userId, String topicId);
}
