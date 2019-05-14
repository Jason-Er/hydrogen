package com.thumbstage.hydrogen.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.IMMessageEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface IMMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IMMessageEntity entity);

    @Update
    void update(IMMessageEntity... atMeEntities);

    @Delete
    void delete(IMMessageEntity... atMeEntities);

    @Query("SELECT * FROM last_im_message WHERE me =:meId ORDER BY is_browsed ASC, last_refresh DESC LIMIT :perPageNum OFFSET :offset")
    LiveData<List<IMMessageEntity>> get(String meId, int perPageNum, int offset);

    @Query("SELECT * FROM last_im_message WHERE me = :meId AND last_refresh > :lastRefreshMax LIMIT 1")
    IMMessageEntity hasIMMessage(String meId, Date lastRefreshMax);

    @Query("UPDATE last_im_message SET is_browsed =:isBrowsed, last_refresh =:lastRefresh WHERE mic_id = :micId AND me = :meId")
    void updateIMMessage(String micId, String meId, int isBrowsed, Date lastRefresh);


}
