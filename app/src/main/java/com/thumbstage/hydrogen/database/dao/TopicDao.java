package com.thumbstage.hydrogen.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.thumbstage.hydrogen.database.entity.TopicEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;

import java.util.Date;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TopicDao {

    @Insert(onConflict = REPLACE)
    void save(TopicEntity topic);

    @Query("SELECT * FROM topic WHERE id = :id")
    LiveData<TopicEntity> load(String id);

    @Query("SELECT * FROM topic WHERE name = :username AND lastRefresh > :lastRefreshMax LIMIT 1")
    UserEntity hasUser(String username, Date lastRefreshMax);
}