package com.bignerdranch.android.testpdfreader.model.storage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.bignerdranch.android.testpdfreader.model.storage.db.BookDbSchema.BookTable.NAME;

public class BookBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 2;
    private static final String DATABASE_NAME = "engReader.db";

    public BookBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "  + NAME + "(" +
                "_id integer primary key autoincrement, " +
                        BookDbSchema.BookTable.Cols.URI + "," +
                        BookDbSchema.BookTable.Cols.TYPE + "," +
                        BookDbSchema.BookTable.Cols.STATE + "," +
                        BookDbSchema.BookTable.Cols.DATE_LAST_OPENED +
                        ")"
                );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
