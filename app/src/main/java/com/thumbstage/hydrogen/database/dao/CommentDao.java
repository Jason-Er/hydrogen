package com.thumbstage.hydrogen.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.CommentEntity;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CommentDao {

    @Insert(onConflict = REPLACE)
    long insert(CommentEntity commentEntity);

    @Insert(onConflict = REPLACE)
    void insert(List<CommentEntity> commentEntities);

    @Update
    void update(CommentEntity... commentEntities);

    @Delete
    void delete(CommentEntity... commentEntities);

}
