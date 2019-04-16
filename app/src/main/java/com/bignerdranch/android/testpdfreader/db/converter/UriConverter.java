package com.bignerdranch.android.testpdfreader.db.converter;

import android.net.Uri;

import androidx.room.TypeConverter;

public class UriConverter {

    @TypeConverter
    public String fromUri(Uri value) {
        return value == null ? null : value.toString();
    }

    @TypeConverter
    public Uri stringToUri(String date) {
        return date == null ? null : Uri.parse(date);
    }

}
