package com.bignerdranch.android.testpdfreader.model.resource_loader.impl;

import android.content.Context;
import android.net.Uri;
import android.os.Looper;

import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;
import com.bignerdranch.android.testpdfreader.model.resource_loader.ResourceLoader;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.handler.PageLoadingHandlerFactory;

import java.io.IOException;
import java.util.List;

import android.os.Handler;

public class ResourceLoaderImpl implements ResourceLoader,
        PageLoadingHandler.OnLoadedListener<OnPageLoadedListener> {

    private PageLoadingHandler<OnPageLoadedListener> mPageLoadingHandler;
    private Uri uri;

    public ResourceLoaderImpl(Uri uri, Context context, Handler handler) throws IOException {
        this.uri = uri;

        PageLoadingHandlerFactory factory = new PageLoadingHandlerFactory(context);

        mPageLoadingHandler = factory.newInstance(uri, handler, this);
        if(mPageLoadingHandler == null) {
            throw new IOException("Format of ResourceLoaderImpl not defined");
        }
    }

    @Override
    public void close() {
        mPageLoadingHandler.quit();
        mPageLoadingHandler.clearQueue();
    }

    @Override
    public void start() {
        mPageLoadingHandler.start();
        mPageLoadingHandler.getLooper();
    }

    @Override
    public int getPagesCount() {
        return mPageLoadingHandler.getPageCount();
    }

    @Override
    public void loadPage(int page, OnPageLoadedListener loadListener) {
        mPageLoadingHandler.addToQueue(loadListener, page);
    }

    @Override
    public void onLoaded(OnPageLoadedListener target, List<String> result) {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = () -> target.onPageLoaded(result);
        handler.postDelayed(runnable, 0);
    }

    @Override
    public Uri getResourceUri() {
        return uri;
    }
}
