package com.thumbstage.hydrogen.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.MicEntity;
import com.thumbstage.hydrogen.database.entity.UserAttendEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface UserAttendDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserAttendEntity entity);

    @Update
    void update(UserAttendEntity... atMeEntities);

    @Delete
    void delete(UserAttendEntity... atMeEntities);

    @Query("SELECT * FROM user_attend WHERE user_id = :userId AND last_refresh > :lastRefreshMax LIMIT 1")
    UserAttendEntity hasEntity(String userId, Date lastRefreshMax);

    @Query("SELECT * FROM mic INNER JOIN topic ON mic.topic_id = topic.id " +
            "WHERE id IN ( SELECT mic_id FROM user_attend WHERE user_id =:userId ORDER BY last_refresh DESC ) " +
            "AND is_finished =:isFinished ORDER BY last_refresh DESC LIMIT :perPageNum OFFSET :offset")
    LiveData<List<MicEntity>> getUserAttend(String userId, boolean isFinished, int perPageNum, int offset);
}
