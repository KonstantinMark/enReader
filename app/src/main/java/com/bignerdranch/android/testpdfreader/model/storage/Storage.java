package com.bignerdranch.android.testpdfreader.model.storage;

import android.content.Context;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;

import java.util.List;

public class Storage {
    private static String TAG = "Storage";

    private static Storage mInstance;

    private AppDatabase db;

    private Storage(Context context) {
        db = AppDatabase.getDatabase(context);
    }

    public static Storage instance(Context context) {
        if(mInstance == null){
            mInstance = new Storage(context);
        }
        return mInstance;
    }

    public long insert(Resource resource) {
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
        db.resourceDao().delete(resource);
        return 0;
    }

    public List<Resource> getAll(){
        return db.resourceDao().loadAllResourcesSync();
    }

    public long update(Resource resource) {
        db.resourceDao().update(resource);
        return 0;
    }

    public Resource get(Uri uri) {
        return db.resourceDao().loadResourceSync(uri);
    }

}
