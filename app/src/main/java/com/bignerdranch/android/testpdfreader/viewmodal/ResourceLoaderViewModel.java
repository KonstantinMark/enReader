package com.bignerdranch.android.testpdfreader.viewmodal;

import android.app.Application;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import com.bignerdranch.android.testpdfreader.model.resource_loader.ResourceLoader;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.ResourceLoaderImpl;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.OnResourceLoadErrorListener;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class ResourceLoaderViewModel extends AndroidViewModel {

    private MediatorLiveData<ResourceLoader> mResourceLoader;

    public ResourceLoaderViewModel(@NonNull Application application) {
        super(application);
        mResourceLoader = new MediatorLiveData<>();
        mResourceLoader.setValue(null);
    }

    public void load(Uri uri, OnResourceLoadErrorListener errorListener){
        Handler handler = new Handler();
        AsyncTask.execute(() ->{
            ResourceLoader loader;
            try {
                loader = new ResourceLoaderImpl(uri, getApplication(), handler);
                loader.start();
            } catch (IOException e) {
                errorListener.onLoadError(e);
                return;
            }
            new Handler(Looper.getMainLooper())
                    .postDelayed(()-> mResourceLoader.setValue(loader), 0 );
        });
    }

    public LiveData<ResourceLoader> getResourceLoader(){
        return mResourceLoader;
    }

    public void close(){
        try {
            mResourceLoader.getValue().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
