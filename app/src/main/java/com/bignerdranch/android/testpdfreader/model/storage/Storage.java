package com.bignerdranch.android.testpdfreader.model.storage;

import android.content.Context;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;

import java.util.List;

public class Storage {
    private static String TAG = "Storage";

    private static Storage mInstance;

//    private SQLiteDatabase mDatabase;
    private AppDatabase db;

    private Storage(Context context) {
//        db =  Room.databaseBuilder(context,
//                AppDatabase.class, "database-name").build();
//        mDatabase = new BookBaseHelper(mContext).getWritableDatabase();
        db = AppDatabase.getDatabase(context);
    }

    public static Storage instance(Context context) {
        if(mInstance == null){
            mInstance = new Storage(context);
        }
        return mInstance;
    }

    public long insert(Resource resource) {
//        if (contains(resource)) return 0;
//        ContentValues values = getContentValues(resource);
//        return mDatabase.insert(BookDbSchema.BookTable.NAME, null, values);
        db.resourceDao().insert(resource);
        return 0;
    }

    public boolean contains(Resource resource) {
        return contains(resource.uri);
    }

    public boolean contains(Uri uri){
        for(Resource r: getAll()){
            if(r.getUri().equals(uri))
                return true;
        }
        return false;
    }

    public int remove(Resource resource) {
//        return remove(resource.getUri());
        db.resourceDao().delete(resource);
        return 0;
    }

//    public int remove(Uri uri) {
////        return mDatabase.delete(BookDbSchema.BookTable.NAME,
////                BookDbSchema.BookTable.Cols.URI + " = ?",
////                new String[]{uri.toString()});
//        db.resourceDao().delete();
//
//    }

    public List<Resource> getAll(){
//        List<Resource> resources = new ArrayList<>();
//        BookCursorWrapper cursor  = queryBooks(null, null);
//
//        try {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                resources.insert(new ResourceBuilder(mContext).build(cursor));
//                cursor.moveToNext();
//            }
//        } finally {
//            cursor.close();
//        }
//        return resources;
        return db.resourceDao().loadAllResourcesSync();
    }

//    private static ContentValues getContentValues(Resource resource) {
//        ContentValues values = new ContentValues();
//        values.put(BookDbSchema.BookTable.Cols.URI, resource.getUri().toString());
//        values.put(BookDbSchema.BookTable.Cols.TYPE, resource.getType().toString());
//        values.put(BookDbSchema.BookTable.Cols.CURRENT_PAGE, resource.getMetaData().getCurrentPage());
//        values.put(BookDbSchema.BookTable.Cols.ITEM_ON_PAGE, resource.getMetaData().getItemOnPage());
//        values.put(BookDbSchema.BookTable.Cols.DATE_LAST_OPENED, resource.getMetaData().getTimeLastOpened().toString());
//        return values;
//    }

    public long update(Resource resource) {
//        if (!contains(resource)) return 0;
//        ContentValues values = getContentValues(resource);
//        return mDatabase.update(BookDbSchema.BookTable.NAME, values,
//                BookDbSchema.BookTable.Cols.URI + " = ?",
//                new String[]{resource.getUri().toString()}
//        );
        db.resourceDao().update(resource);
        return 0;
    }

//    private BookCursorWrapper queryBooks(String whereClause, String[] whereArgs){
//        return new BookCursorWrapper(mDatabase.query(
//                BookDbSchema.BookTable.NAME,
//                null,
//                whereClause,
//                whereArgs,
//                null,
//                null,
//                null
//        ));
//    }

    public Resource get(Uri uri) {
//        if (uri == null) return null;
//        BookCursorWrapper cursor = queryBooks(null, null);
//        try {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                if (uri.equals(cursor.getUri())) {
//                    return new ResourceBuilder(mContext).build(cursor);
//                }
//                cursor.moveToNext();
//            }
//        } finally {
//            cursor.close();
//        }
        return db.resourceDao().loadResourceSync(uri);
    }

}
