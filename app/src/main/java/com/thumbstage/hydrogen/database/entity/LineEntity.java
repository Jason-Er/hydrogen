package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "line",
        primaryKeys = {"who","when", "in_which_topic"},
        indices = {@Index("in_which_topic"), @Index("who")},
        foreignKeys = {
                @ForeignKey(entity = TopicEntity.class,
                        parentColumns = "id",
                        childColumns = "in_which_topic",
                        onDelete = CASCADE),
                @ForeignKey(entity = UserEntity.class,
                        parentColumns = "id",
                        childColumns = "who")})
public class LineEntity {

    @NonNull
    private String who;
    @NonNull
    private Date when;
    private String what;

    @ColumnInfo(name = "line_type")
    private String line_type;
    @NonNull
    @ColumnInfo(name = "in_which_topic")
    private String inWhichTopic;

    private long ordinal;

    @NonNull
    public String getWho() {
        return who;
    }

    public void setWho(@NonNull String who) {
        this.who = who;
    }
    @NonNull
    public Date getWhen() {
        return when;
    }

    public void setWhen(@NonNull Date when) {
        this.when = when;
    }

    public String getWhat() {
        return what;
    }

    public void setWhat(String what) {
        this.what = what;
    }

    public String getLine_type() {
        return line_type;
    }

    public void setLine_type(String line_type) {
        this.line_type = line_type;
    }

    public String getInWhichTopic() {
        return inWhichTopic;
    }

    public void setInWhichTopic(String inWhichTopic) {
        this.inWhichTopic = inWhichTopic;
    }

    public long getOrdinal() {
        return ordinal;
    }

    public void setOrdinal(long ordinal) {
        this.ordinal = ordinal;
    }
}
