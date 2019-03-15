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
        indices = {@Index("in_which_topic")},
        foreignKeys = {
                @ForeignKey(entity = TopicEntity.class,
                        parentColumns = "id",
                        childColumns = "in_which_topic",
                        onDelete = CASCADE)})
public class LineEntity {
    @PrimaryKey(autoGenerate = true)
    protected long id;
    @NonNull
    protected String who;
    protected Date when;
    protected String what;

    @ColumnInfo(name = "line_type")
    protected String line_type;
    @ColumnInfo(name = "in_which_topic")
    protected String inWhichTopic;

    @NonNull
    public String getWho() {
        return who;
    }

    public void setWho(@NonNull String who) {
        this.who = who;
    }

    public Date getWhen() {
        return when;
    }

    public void setWhen(Date when) {
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
