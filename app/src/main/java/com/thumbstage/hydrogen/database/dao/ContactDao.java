package com.thumbstage.hydrogen.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.thumbstage.hydrogen.database.entity.ContactEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ContactDao {

    @Insert(onConflict = REPLACE)
    void insert(ContactEntity contract);

    @Insert(onConflict = REPLACE)
    void insert(List<ContactEntity> contactEntities);

    @Update
    void update(ContactEntity... contactEntities);

    @Delete
    void delete(ContactEntity... contactEntities);

    @Query("SELECT * FROM user WHERE id IN (SELECT contact_id from contact WHERE user_id = :meId) ORDER BY name COLLATE LOCALIZED ASC LIMIT :perPageNum OFFSET :offset")
    LiveData<List<UserEntity>> get(String meId, int perPageNum, int offset);

    @Query("SELECT * FROM contact WHERE user_id = :userId AND last_refresh > :lastRefreshMax LIMIT 1")
    ContactEntity hasContract(String userId, Date lastRefreshMax);
}