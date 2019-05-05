package com.bignerdranch.android.testpdfreader.viewmodal;

import android.app.Application;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;

import com.bignerdranch.android.testpdfreader.model.resource_loader.ResourceLoader;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.ResourceLoaderCachedWrapper;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.ResourceLoaderImpl;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

public class ResourceStorageLoaderViewModel extends AndroidViewModel {

    private static MediatorLiveData<ResourceLoader> mResourceLoader;

    public ResourceStorageLoaderViewModel(@NonNull Application application) {
        super(application);
        if(mResourceLoader == null) {
            mResourceLoader = new MediatorLiveData<>();
            mResourceLoader.setValue(null);
        }
    }

    public void load(Uri uri, Handler handler) throws IOException {

        ResourceLoader loader;

        if(mResourceLoader.getValue() == null) {
            loader = new ResourceLoaderCachedWrapper(
                    new ResourceLoaderImpl(uri, getApplication(), handler));
        } else {
            loader = mResourceLoader.getValue();
            if(!loader.getResourceUri().equals(uri)){
                ((ResourceLoaderCachedWrapper) loader).clearCash();
            }

            ((ResourceLoaderCachedWrapper) loader).setImpl(
                    new ResourceLoaderImpl(uri, getApplication(), handler)
            );
        }

        loader.start();

        ResourceLoader finalLoader = loader;
        new Handler(Looper.getMainLooper())
                    .postDelayed(() -> mResourceLoader.setValue(finalLoader), 0);
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
