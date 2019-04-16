package com.bignerdranch.android.testpdfreader.db;

import android.content.Context;

import com.bignerdranch.android.testpdfreader.db.dao.MetaDataDao;
import com.bignerdranch.android.testpdfreader.db.dao.ResourceDao;
import com.bignerdranch.android.testpdfreader.db.entry.MetaData;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Resource.class, MetaData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract ResourceDao resourceDao();
    public abstract MetaDataDao metaDataDao();

    public static AppDatabase getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context,
                    AppDatabase.class, "database-name").build();
        }
        return INSTANCE;
    }

//    public static AppDatabase getInMemoryDatabase(Context context) {
//        if (INSTANCE == null) {
//            INSTANCE =
//                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
//                            .build();
//        }
//        return INSTANCE;
//    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
