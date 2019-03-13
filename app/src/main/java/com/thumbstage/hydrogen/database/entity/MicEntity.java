package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "mic")
public class MicEntity {
    @PrimaryKey
    @NonNull
    protected String id;
}
