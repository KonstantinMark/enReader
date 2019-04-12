package com.bignerdranch.android.testpdfreader.model.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.model.storage.db.BookBaseHelper;
import com.bignerdranch.android.testpdfreader.model.storage.db.BookCursorWrapper;
import com.bignerdranch.android.testpdfreader.model.storage.db.BookDbSchema;
import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;
import com.bignerdranch.android.testpdfreader.model.storage.resource.ResourceBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Storage {
    private static String TAG = "Storage";

    private static Storage mInstance;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private Storage(Context context) {
        mContext = context;
        mDatabase = new BookBaseHelper(mContext).getWritableDatabase();
    }

    public static Storage instance(Context context) {
        if(mInstance == null){
            mInstance = new Storage(context);
        }
        return mInstance;
    }

    private static ContentValues getContentValues(IResource resource) {
        ContentValues values = new ContentValues();
        values.put(BookDbSchema.BookTable.Cols.URI, resource.getUri().toString());
        values.put(BookDbSchema.BookTable.Cols.TYPE, resource.getType().toString());
        values.put(BookDbSchema.BookTable.Cols.STATE, resource.getMetaData().getState());
        values.put(BookDbSchema.BookTable.Cols.DATE_LAST_OPENED, resource.getMetaData().getTimeLastOpened().toString());
        return values;
    }

    public long add(IResource resource) {
        if (contains(resource)) return 0;
        ContentValues values = getContentValues(resource);
        return mDatabase.insert(BookDbSchema.BookTable.NAME, null, values);
    }

    public boolean contains(IResource resource) {
        if (resource == null) return false;
        for (IResource r : getAll()) {
            if (r.equals(resource)) return true;
        }
        return false;
    }

    public int remove(IResource resource) {
        return remove(resource.getUri());
    }

    public boolean contains(Uri uri){
        for(IResource r: getAll()){
            if(r.getUri().equals(uri))
                return true;
        }
        return false;
    }

    public int remove(Uri uri) {
        return mDatabase.delete(BookDbSchema.BookTable.NAME,
                BookDbSchema.BookTable.Cols.URI + " = ?",
                new String[]{uri.toString()});
    }

    public List<IResource> getAll(){
        List<IResource> resources = new ArrayList<>();
        BookCursorWrapper cursor  = queryBooks(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                resources.add(new ResourceBuilder(mContext).build(cursor));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        Collections.sort(resources);
        Collections.reverse(resources);
        return resources;
    }

    public IResource get(Uri uri) {
        BookCursorWrapper cursor = queryBooks(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (uri.equals(cursor.getUri())) {
                    return new ResourceBuilder(mContext).build(cursor);
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return null;
    }

    public long update(IResource resource) {
        if (!contains(resource)) return 0;
        ContentValues values = getContentValues(resource);
        return mDatabase.update(BookDbSchema.BookTable.NAME, values,
                BookDbSchema.BookTable.Cols.URI + " = ?",
                new String[]{resource.getUri().toString()}
        );
    }

    private BookCursorWrapper queryBooks(String whereClause, String[] whereArgs){
        return new BookCursorWrapper(mDatabase.query(
                BookDbSchema.BookTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        ));
    }

}
