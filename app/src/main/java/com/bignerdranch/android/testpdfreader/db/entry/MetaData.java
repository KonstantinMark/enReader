package com.bignerdranch.android.testpdfreader.db.entry;

import android.net.Uri;

import com.bignerdranch.android.testpdfreader.db.converter.DateConverter;
import com.bignerdranch.android.testpdfreader.db.converter.UriConverter;

import java.sql.Timestamp;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "metaData")
@TypeConverters({DateConverter.class, UriConverter.class})
public class MetaData {
    @NonNull
    @PrimaryKey
    public  Uri uri;
    public int currentPage;
    public int currentItem;
    public Timestamp lastOpenedTime;

    @NonNull
    public Uri getUri() {
        return uri;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public int getCurrentItem() {
        return currentItem;
    }

    public Timestamp getLastOpenedTime() {
        return lastOpenedTime;
    }
}
