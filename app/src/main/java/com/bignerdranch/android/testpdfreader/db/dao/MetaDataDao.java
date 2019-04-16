package com.bignerdranch.android.testpdfreader.db.dao;

import android.net.Uri;

import com.bignerdranch.android.testpdfreader.db.converter.UriConverter;
import com.bignerdranch.android.testpdfreader.db.entry.MetaData;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

@Dao
@TypeConverters(UriConverter.class)
public interface MetaDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MetaData data);

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
