package com.thumbstage.hydrogen.database.converter;

import android.arch.persistence.room.TypeConverter;

import com.thumbstage.hydrogen.model.bo.LineType;

public class LineTypeConverter {
    @TypeConverter
    public static LineType toLineType(String type) {
        return type == null ? null : LineType.valueOf(type);
    }

    @TypeConverter
    public static String toString(LineType type) {
        return type == null ? null : type.name();
    }
}
