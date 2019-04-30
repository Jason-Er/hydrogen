package com.thumbstage.hydrogen.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.AtMeEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface AtMeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AtMeEntity entity);

    @Update
    void update(AtMeEntity... atMeEntities);

    @Delete
    void delete(AtMeEntity... atMeEntities);

    @Query("SELECT * FROM at_me WHERE me =:meId ORDER BY is_browsed ASC, last_refresh DESC LIMIT :perPageNum OFFSET :offset")
    LiveData<List<AtMeEntity>> get(String meId, int perPageNum, int offset);

    @Query("SELECT * FROM at_me WHERE me = :meId AND last_refresh > :lastRefreshMax LIMIT 1")
    AtMeEntity hasAtMe(String meId, Date lastRefreshMax);

    @Query("DELETE FROM at_me WHERE mic_id = :micId AND me = :meId")
    void deleteAtMeByKey(String micId, String meId);

    @Query("UPDATE at_me SET is_browsed =:isBrowsed, last_refresh =:lastRefresh WHERE mic_id = :micId AND me = :meId")
    void updateAtMe(String micId, String meId, int isBrowsed, Date lastRefresh);


}
