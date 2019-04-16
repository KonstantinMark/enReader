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

    public static AppDatabase getInMemoryDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                            // To simplify the codelab, allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
