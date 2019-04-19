package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "contact",
        primaryKeys = {"user_id","contact_id"},
        indices = {@Index("user_id"), @Index("contact_id")},
        foreignKeys = {
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "user_id",
                        onDelete = CASCADE),
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "contact_id")})
public class ContactEntity {
    @ColumnInfo(name = "user_id")
    @NonNull
    private String userId;
    @ColumnInfo(name = "contact_id")
    @NonNull
    private String contractId;
    @ColumnInfo(name = "last_refresh")
    private Date lastRefresh;

    @NonNull
    public String getUserId() {
        return userId;
    }

    public void setUserId(@NonNull String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getContractId() {
        return contractId;
    }

    public void setContractId(@NonNull String contractId) {
        this.contractId = contractId;
    }

    public Date getLastRefresh() {
        return lastRefresh;
    }

    public void setLastRefresh(Date lastRefresh) {
        this.lastRefresh = lastRefresh;
    }
}
