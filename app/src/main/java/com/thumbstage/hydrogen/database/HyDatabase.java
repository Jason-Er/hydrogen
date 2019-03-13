package com.thumbstage.hydrogen.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.thumbstage.hydrogen.database.converter.DateConverter;
import com.thumbstage.hydrogen.database.dao.UserDao;
import com.thumbstage.hydrogen.database.entity.LineEntity;
import com.thumbstage.hydrogen.database.entity.MicEntity;
import com.thumbstage.hydrogen.database.entity.TopicEntity;
import com.thumbstage.hydrogen.database.entity.TopicExEntity;
import com.thumbstage.hydrogen.database.entity.TopicUserEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;


@Database(entities = {
        UserEntity.class,
        LineEntity.class,
        TopicEntity.class,
        TopicExEntity.class,
        TopicUserEntity.class,
        MicEntity.class }, version = 1)
@TypeConverters({DateConverter.class})
public abstract class HyDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile HyDatabase INSTANCE;

    // --- DAO ---
    public abstract UserDao userDao();
}
