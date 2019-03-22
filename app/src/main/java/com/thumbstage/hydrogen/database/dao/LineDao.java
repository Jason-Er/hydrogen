package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.LineEntity;

import java.util.List;

@Dao
public interface LineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LineEntity lineEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<LineEntity> lineEntities);

    @Update
    void update(LineEntity... lineEntities);

    @Delete
    void delete(LineEntity... lineEntities);

    @Query("SELECT * FROM line WHERE in_which_topic =:topicId ORDER BY ordinal ASC")
    List<LineEntity> get(String topicId);

}
