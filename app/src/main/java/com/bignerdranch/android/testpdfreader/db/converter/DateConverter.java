package com.bignerdranch.android.testpdfreader.db.converter;

import java.sql.Timestamp;
import java.util.Date;

import androidx.room.TypeConverter;

public class DateConverter {
    @TypeConverter
    public Timestamp fromTimestamp(Long value) {
        return value == null ? null : new Timestamp(value);
    }

    @TypeConverter
    public Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
