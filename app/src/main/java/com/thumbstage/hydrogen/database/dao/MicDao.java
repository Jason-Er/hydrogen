package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.MicEntity;

@Dao
public interface MicDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MicEntity micEntity);

    @Update
    void update(MicEntity... micEntities);

    @Delete
    void delete(MicEntity... micEntities);

    @Query("SELECT * FROM mic WHERE id = :id")
    MicEntity get(String id);
}
