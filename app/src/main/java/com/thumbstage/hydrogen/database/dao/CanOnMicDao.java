package com.thumbstage.hydrogen.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.CanOnMicEntity;

import java.util.List;

@Dao
public interface CanOnMicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CanOnMicEntity entity);

    @Update
    void update(CanOnMicEntity... atMeEntities);

    @Delete
    void delete(CanOnMicEntity... atMeEntities);

    @Query("SELECT * FROM can_on_mic WHERE user_id =:userId AND mic_id =:micId")
    LiveData<List<CanOnMicEntity>> getCanOnMic(String userId, String micId);
}
