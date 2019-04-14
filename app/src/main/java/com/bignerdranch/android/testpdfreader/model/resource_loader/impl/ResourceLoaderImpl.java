package com.bignerdranch.android.testpdfreader.model.resource_loader.impl;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;
import com.bignerdranch.android.testpdfreader.model.resource_loader.ResourceLoader;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.handler.PageLoadingHandlerFactory;

import java.io.IOException;

public class ResourceLoaderImpl implements ResourceLoader {

    private PageLoadingHandler<OnPageLoadedListener> mPageLoadingHandler;

    public ResourceLoaderImpl(Uri uri, Context context) throws IOException {
        Log.i("MY_TAG", "ResourceLoaderImpl");
        PageLoadingHandlerFactory factory = new PageLoadingHandlerFactory(context);
        Log.i("MY_TAG", "ResourceLoaderImpl_1");
        mPageLoadingHandler = factory.newInstance(uri);
        Log.i("MY_TAG", "ResourceLoaderImpl_1...");
        if(mPageLoadingHandler == null) {
            Log.i("MY_TAG", "ResourceLoaderImpl_1...Error");
            throw  new IOException("Format of ResourceLoaderImpl not defined");
        }
        Log.i("MY_TAG", "ResourceLoaderImpl_2");
        mPageLoadingHandler.start();
        Log.i("MY_TAG", "ResourceLoaderImpl_3");
        mPageLoadingHandler.getLooper();
        Log.i("MY_TAG", "ResourceLoaderImpl...");
    }

    @Override
    public void close() {
        mPageLoadingHandler.quit();
        mPageLoadingHandler.clearQueue();
    }

    @Override
    public int getPagesCount() {
        return mPageLoadingHandler.getPageCount();
    }

    @Override
    public void loadPage(int page, OnPageLoadedListener loadListener) {
        mPageLoadingHandler.addToQueue(loadListener, page);
    }
}
