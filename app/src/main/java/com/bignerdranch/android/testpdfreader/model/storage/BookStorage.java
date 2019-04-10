package com.bignerdranch.android.testpdfreader.model.storage;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.model.storage.db.BookBaseHelper;
import com.bignerdranch.android.testpdfreader.model.storage.db.BookCursorWrapper;
import com.bignerdranch.android.testpdfreader.model.storage.db.BookDbSchema;
import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;
import com.bignerdranch.android.testpdfreader.model.storage.resource.ResourceBuilder;

import java.util.ArrayList;
import java.util.List;

public class BookStorage {
    private static String TAG = "BookStorage";

    private static BookStorage mInstance;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static BookStorage instance(Context context){
        if(mInstance == null){
            mInstance = new BookStorage(context);
        }

        return mInstance;
    }

    private BookStorage(Context context){
        mContext = context;
        mDatabase = new BookBaseHelper(mContext).getWritableDatabase();
    }


    public void addPdfUri(Uri uri){
        if(contains(uri)) return;

        grantPermissions(uri);
        IResource resource = ResourceBuilder.build(uri, mContext);
        ContentValues values = getContentValues(resource);

        mDatabase.insert(BookDbSchema.BookTable.NAME, null, values);
    }

    public void remove(Uri uri) {
        if (!contains(uri)) return;
        mDatabase.delete(BookDbSchema.BookTable.NAME,
                BookDbSchema.BookTable.Cols.URI + " = ?",
                new String[]{uri.toString()});
    }

    public boolean contains(Uri uri){
        for(IResource r: getAll()){
            if(r.getUri().equals(uri))
                return true;
        }
        return false;
    }

    public List<IResource> getAll(){
        List<IResource> resources = new ArrayList<>();
        BookCursorWrapper cursor  = queryBooks(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Uri uri = cursor.getUri();
                resources.add(ResourceBuilder.build(uri, mContext));
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return resources;
    }

    public IResource get(Uri uri) {
        BookCursorWrapper cursor = queryBooks(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                if (uri.equals(cursor.getUri())) {
                    return ResourceBuilder.build(uri, mContext);
                }
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return null;
    }

    private void grantPermissions(Uri uri){
        mContext.grantUriPermission(mContext.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }


    private static ContentValues getContentValues(IResource resource){
        ContentValues values = new ContentValues();
        values.put(BookDbSchema.BookTable.Cols.URI, resource.getUri().toString());
        values.put(BookDbSchema.BookTable.Cols.TYPE, resource.getType().toString());

        return values;
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
