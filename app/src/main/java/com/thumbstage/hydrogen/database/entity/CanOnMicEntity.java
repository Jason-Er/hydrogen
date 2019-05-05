package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;


import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "can_on_mic",
        primaryKeys = {"user_id","mic_id", "can"},
        indices = {@Index("user_id"), @Index("mic_id")},
        foreignKeys = {
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = CASCADE),
                @ForeignKey(entity = MicEntity.class,
                        parentColumns = "id",
                        childColumns = "mic_id")})
public class CanOnMicEntity {
    @NonNull
    @ColumnInfo(name = "user_id")
    String userId;
    @NonNull
    @ColumnInfo(name = "mic_id")
    String micId;
    @NonNull
    String can;
    @NonNull
    @ColumnInfo(name = "last_refresh")
    Date lastRefresh;

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getMicId() {
        return micId;
    }

    public void setMicId(@NonNull String micId) {
        this.micId = micId;
    }

    @NonNull
    public String getCan() {
        return can;
    }

    public void setCan(@NonNull String can) {
        this.can = can;
    }

    @NonNull
    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(@NonNull Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
}
