package com.thumbstage.hydrogen.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.MicEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface MicDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MicEntity micEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(List<MicEntity> micEntities);

    @Update
    void update(MicEntity... micEntities);

    @Delete
    void delete(MicEntity... micEntities);

    @Query("SELECT * FROM mic WHERE id = :id")
    MicEntity get(String id);

    @Query("SELECT id FROM mic WHERE id = :id LIMIT 1")
    String getItemId(String id);

    @Query("SELECT * FROM mic WHERE id IN (:ids)")
    List<MicEntity> get(List<String> ids);

    @Query("SELECT * FROM mic WHERE topic_id IN ( SELECT id FROM topic WHERE id IN ( SELECT topic_id FROM topic_tag WHERE tag =:tag) AND is_finished =:isFinished) ORDER BY update_at DESC LIMIT :perPageNum OFFSET :offset")
    LiveData<List<MicEntity>> get(String tag, boolean isFinished, int perPageNum,  int offset);

    @Query("SELECT * FROM mic WHERE topic_id IN ( SELECT id FROM topic WHERE id IN ( SELECT topic_id FROM topic_tag WHERE tag =:tag) AND id IN ( SELECT topic_id FROM topic_user WHERE user_id =:userId ) AND is_finished =:isFinished ) ORDER BY has_new DESC, update_at DESC LIMIT :perPageNum OFFSET :offset")
    LiveData<List<MicEntity>> get(String tag, String userId, boolean isFinished, int perPageNum, int offset);

    @Query("SELECT * FROM mic WHERE topic_id IN ( SELECT id FROM topic WHERE id IN ( SELECT topic_id FROM topic_tag WHERE tag =:tag) AND is_finished =:isFinished) ORDER BY update_at DESC")
    DataSource.Factory<Integer, MicEntity> get(String tag, boolean isFinished);

    @Query("SELECT * FROM mic WHERE topic_id IN ( SELECT id FROM topic WHERE id IN ( SELECT topic_id FROM topic_tag WHERE tag =:tag) AND id IN ( SELECT topic_id FROM topic_user WHERE user_id =:userId ) AND is_finished =:isFinished ) ORDER BY has_new DESC, update_at DESC")
    DataSource.Factory<Integer, MicEntity> get(String tag, String userId, boolean isFinished);

    @Query("UPDATE mic SET topic_id =:topicId, last_refresh =:lastFresh WHERE id =:id")
    void update(String id, String topicId, Date lastFresh);

    @Query("UPDATE mic SET has_new =:hasNew, last_refresh =:lastRefresh WHERE id = :micId")
    void updateHasNew(String micId, int hasNew, Date lastRefresh);

}
