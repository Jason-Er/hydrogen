package com.thumbstage.hydrogen.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;


import com.thumbstage.hydrogen.database.entity.UserEntity;

import java.util.Date;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Insert(onConflict = REPLACE)
    void save(UserEntity user);

    @Query("SELECT * FROM user WHERE name = :username")
    LiveData<UserEntity> load(String username);

    @Query("SELECT * FROM user WHERE name = :username AND lastRefresh > :lastRefreshMax LIMIT 1")
    UserEntity hasUser(String username, Date lastRefreshMax);
}