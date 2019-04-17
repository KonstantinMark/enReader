package com.bignerdranch.android.testpdfreader.db.dao;

import android.net.Uri;

import com.bignerdranch.android.testpdfreader.db.converter.UriConverter;
import com.bignerdranch.android.testpdfreader.db.entry.MetaData;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;

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
@TypeConverters(UriConverter.class)
public interface ResourceDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void insert(Resource resource);

    @Update
    void update(Resource resource);

    @Query("SELECT * " +
            "FROM resource r inner join metaData m on r.uri = m.uri " +
            "order by m.lastOpenedTime desc" )
    LiveData<List<Resource>> loadAllResources();

    @Query("SELECT * " +
            "FROM resource r left join metaData m on r.uri = m.uri " +
            "order by m.lastOpenedTime")
    List<Resource> loadAllResourcesSync();

    @Query("SELECT * from resource where uri = :uri")
     Resource loadResourceSync(Uri uri);

    @Query("SELECT * from resource where uri = :uri")
    LiveData<Resource> loadResource(Uri uri);

    @Delete
    void delete(Resource resource);

}
