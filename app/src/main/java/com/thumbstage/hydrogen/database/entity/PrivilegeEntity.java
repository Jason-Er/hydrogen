package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

@Entity(tableName = "Privilege",
        primaryKeys = { "user_id", "privilege" },
        indices = {@Index("user_id")},
        foreignKeys = {
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "user_id")})
public class PrivilegeEntity {

    @ColumnInfo(name = "user_id")
    private String userId;
    @ColumnInfo(name = "privilege")
    private String privilege;

}
