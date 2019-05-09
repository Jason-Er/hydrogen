package com.thumbstage.hydrogen.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.thumbstage.hydrogen.database.converter.DateConverter;
import com.thumbstage.hydrogen.database.dao.AtMeDao;
import com.thumbstage.hydrogen.database.dao.ContactDao;
import com.thumbstage.hydrogen.database.dao.LineDao;
import com.thumbstage.hydrogen.database.dao.MicDao;
import com.thumbstage.hydrogen.database.dao.TopicDao;
import com.thumbstage.hydrogen.database.dao.TopicTagDao;
import com.thumbstage.hydrogen.database.dao.TopicUserDao;
import com.thumbstage.hydrogen.database.dao.UserDao;
import com.thumbstage.hydrogen.database.entity.AtMeEntity;
import com.thumbstage.hydrogen.database.entity.ContactEntity;
import com.thumbstage.hydrogen.database.entity.LineEntity;
import com.thumbstage.hydrogen.database.entity.MicEntity;
import com.thumbstage.hydrogen.database.entity.TopicEntity;
import com.thumbstage.hydrogen.database.entity.TopicTagEntity;
import com.thumbstage.hydrogen.database.entity.TopicUserEntity;
import com.thumbstage.hydrogen.database.entity.UserEntity;

@Database(entities = {
        UserEntity.class,
        LineEntity.class,
        TopicEntity.class,
        TopicUserEntity.class,
        MicEntity.class,
        AtMeEntity.class,
        ContactEntity.class,
        TopicTagEntity.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class HyDatabase extends RoomDatabase {

    // --- SINGLETON ---
    private static volatile HyDatabase INSTANCE;

    // --- DAO ---
    public abstract UserDao userDao();
    public abstract LineDao lineDao();
    public abstract MicDao micDao();
    public abstract TopicDao topicDao();
    public abstract TopicUserDao topicUserDao();
    public abstract AtMeDao atMeDao();
    public abstract ContactDao contractDao();
    public abstract TopicTagDao topicTagDao();
}
