package com.bignerdranch.android.testpdfreader.viewmodal;

import android.app.Application;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.db.entry.MetaData;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class ResourceDBViewModel extends AndroidViewModel {

    private MediatorLiveData<Resource> mObservableResource;
    private MediatorLiveData<MetaData> mObservableMataData;

    private AppDatabase db;
    public ResourceDBViewModel(@NonNull Application application) {
        super(application);
        db = AppDatabase.getDatabase(application);
        mObservableResource = new MediatorLiveData<>();
        mObservableMataData = new MediatorLiveData<>();
        mObservableResource.setValue(null);
        mObservableMataData.setValue(null);

    }

    private Uri current;
    public void loadResource(Uri uri){
        if(!uri.equals(current)) {
            current = uri;
            LiveData<Resource> resource = db.resourceDao().loadResource(uri);
            LiveData<MetaData> metaData = db.metaDataDao().loadMetaDate(uri);
            mObservableResource.addSource(resource, mObservableResource::setValue);
            mObservableMataData.addSource(metaData, mObservableMataData::setValue);
        }
    }

    public LiveData<Resource> getResource(){
        return mObservableResource;
    }

    public LiveData<MetaData> getMetaDate(){
        return mObservableMataData;
    }

    public void updateCurrentPage(int currentPage){
        db.metaDataDao().update(current, currentPage, 0);
    }

    public void updateCurrentItem(int item){
        db.metaDataDao().update(current, item);
    }

}
