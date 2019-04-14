package com.bignerdranch.android.testpdfreader.model.resource_loader.impl;

import android.content.Context;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;
import com.bignerdranch.android.testpdfreader.model.resource_loader.ResourceLoader;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.handler.PageLoadingHandlerFactory;

import java.io.IOException;

public class ResourceLoaderImpl implements ResourceLoader {

    private PageLoadingHandler<OnPageLoadedListener> mPageLoadingHandler;

    public ResourceLoaderImpl(Uri uri, Context context) throws IOException {
        PageLoadingHandlerFactory factory = new PageLoadingHandlerFactory(context);
        mPageLoadingHandler = factory.newInstance(uri);
        mPageLoadingHandler.start();
        mPageLoadingHandler.getLooper();
    }

    @Override
    public void close() throws Exception {
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
