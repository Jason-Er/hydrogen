package com.thumbstage.hydrogen.database.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.support.annotation.NonNull;

import com.thumbstage.hydrogen.model.LineType;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;


@Entity(tableName = "line",
        foreignKeys = {
                @ForeignKey(entity = TopicEntity.class,
                        parentColumns = "id",
                        childColumns = "in_which_topic",
                        onDelete = CASCADE)})
public class LineEntity {
    
    @NonNull
    protected String who;
    protected Date when;
    protected String what;

    @ColumnInfo(name = "line_type")
    protected LineType line_type;
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

    public LineType getLine_type() {
        return line_type;
    }

    public void setLine_type(LineType line_type) {
        this.line_type = line_type;
    }

    public String getInWhichTopic() {
        return inWhichTopic;
    }

    public void setInWhichTopic(String inWhichTopic) {
        this.inWhichTopic = inWhichTopic;
    }
}
