package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "user_attend",
        primaryKeys = {"user_id","mic_id"},
        indices = {@Index("user_id"), @Index("mic_id")},
        foreignKeys = {
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = CASCADE),
                @ForeignKey(entity = MicEntity.class,
                        parentColumns = "id",
                        childColumns = "mic_id")})
public class UserAttendEntity {
    @NonNull
    @ColumnInfo(name = "user_id")
    String userId;
    @NonNull
    @ColumnInfo(name = "mic_id")
    String micId;
    @NonNull
    String type;
    @NonNull
    @ColumnInfo(name = "last_refresh")
    Date lastRefresh;
}
