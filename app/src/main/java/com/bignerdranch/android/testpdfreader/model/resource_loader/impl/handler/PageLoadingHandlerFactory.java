package com.bignerdranch.android.testpdfreader.model.resource_loader.impl.handler;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.PageLoadingHandler;
import com.bignerdranch.android.testpdfreader.model.storage.Storage;
import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;

import java.io.IOException;
import java.util.UUID;

public class PageLoadingHandlerFactory {
    private Context mContext;

    public PageLoadingHandlerFactory(Context context) {
        mContext = context;
    }

    public <T extends OnPageLoadedListener> PageLoadingHandler<T> newInstance(Uri uri) throws IOException {
        Log.i("MY_TAG", "PageLoadingHandler__newInstance " + (mContext!=null));
        Storage storage = Storage.instance(mContext);
        Log.i("MY_TAG", "PageLoadingHandler__newInstance_stor");
        IResource resource = storage.get(uri);

        Log.i("MY_TAG", "PageLoadingHandler__newInstance...");

        Handler handler = new Handler(Looper.getMainLooper());
        if (resource != null) {
            switch (resource.getType()) {
                case PDF:
                    return new PageLoadingHandlerPDF<T>(handler, mContext, uri);
                default:
                    return null;
            }
        } else return null;
    }
}