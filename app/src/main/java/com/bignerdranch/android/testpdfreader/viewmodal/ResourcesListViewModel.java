package com.bignerdranch.android.testpdfreader.viewmodal;

import android.app.Application;

import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class ResourcesListViewModel extends AndroidViewModel {
    private MediatorLiveData<List<Resource>> mObservableResources;

    AppDatabase db;

    public ResourcesListViewModel(@NonNull Application application) {
        super(application);
        mObservableResources = new MediatorLiveData<>();
        mObservableResources.setValue(null);

        db = AppDatabase.getDatabase(application);

        LiveData<List<Resource>> resources = db.resourceDao().loadAllResources();
        mObservableResources.addSource(resources, mObservableResources::setValue);
    }

    public LiveData<List<Resource>> getResources(){
        return mObservableResources;
    }

}
