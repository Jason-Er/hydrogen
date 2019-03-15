package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.thumbstage.hydrogen.database.entity.UserEntity;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Insert(onConflict = REPLACE)
    void insert(UserEntity user);

    @Insert(onConflict = REPLACE)
    void insert(List<UserEntity> userEntityList);

    @Update
    void update(UserEntity... userEntities);

    @Delete
    void delete(UserEntity... userEntities);

    @Query("SELECT * FROM user WHERE id = :userId")
    UserEntity get(String userId);

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<UserEntity> get(List<String> userIds);

    @Query("SELECT * FROM user LIMIT :num")
    List<UserEntity> get(int num);

    @Query("SELECT * FROM user WHERE name = :username AND last_refresh > :lastRefreshMax LIMIT 1")
    UserEntity hasUser(String username, Date lastRefreshMax);
}