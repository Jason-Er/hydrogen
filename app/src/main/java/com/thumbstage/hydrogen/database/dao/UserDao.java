package com.thumbstage.hydrogen.database.dao;

import android.arch.lifecycle.LiveData;
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

    @Query("SELECT * FROM user WHERE id = :userId")
    LiveData<UserEntity> getLive(String userId);

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    List<UserEntity> get(List<String> userIds);

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    LiveData<List<UserEntity>> getLive(List<String> userIds);

    @Query("SELECT * FROM user LIMIT :num")
    List<UserEntity> get(int num);

    @Query("SELECT * FROM user WHERE id = :id AND last_refresh > :lastRefreshMax LIMIT 1")
    UserEntity hasUser(String id, Date lastRefreshMax);

    @Query("SELECT * FROM user WHERE id IN (SELECT user_id FROM topic_user WHERE topic_id =(SELECT topic_id FROM mic WHERE id =:micId ))")
    List<UserEntity> getMembers(String micId);
}