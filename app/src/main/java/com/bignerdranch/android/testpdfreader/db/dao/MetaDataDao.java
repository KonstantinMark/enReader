package com.bignerdranch.android.testpdfreader.db.dao;

import android.net.Uri;
import android.util.Log;

import com.bignerdranch.android.testpdfreader.db.converter.DateConverter;
import com.bignerdranch.android.testpdfreader.db.converter.UriConverter;
import com.bignerdranch.android.testpdfreader.db.entry.MetaData;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

@Dao
@TypeConverters({DateConverter.class, UriConverter.class})
public interface MetaDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MetaData data);

    @Update
    void update(MetaData data);

    @Query("UPDATE metaData SET lastOpenedTime = :time where uri = :uri")
    void updateTime(Uri uri, Timestamp time);

    @Query("UPDATE metaData SET currentItem = :currentItem where uri = :uri")
    void update(Uri uri, int currentItem);

    @Query("UPDATE metaData SET currentPage = :currentPage, currentItem = :currentItem" +
            " where uri = :uri")
    void update(Uri uri, int currentPage, int currentItem);

    @Query("SELECT * FROM metaData")
    LiveData<List<MetaData>> loadAllMetaDate();

    @Query("SELECT * FROM metaData")
    List<MetaData> loadAllMetaDateSync();

    @Query("SELECT * from metaData where uri = :uri")
    MetaData loadMetaDateSync(Uri uri);

    @Query("SELECT * from metaData where uri = :uri")
    LiveData<MetaData> loadMetaDate(Uri uri);

    @Delete
    void delete(MetaData data);

}
