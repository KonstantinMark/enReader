package com.bignerdranch.android.testpdfreader.model.resource_loader.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;
import com.bignerdranch.android.testpdfreader.model.resource_loader.ResourceLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceLoaderCachedWrapper implements ResourceLoader {

    @SuppressLint("UseSparseArrays")
    private Map<Integer, List<String>> cash = new HashMap<>();

    private ResourceLoader impl;
    @SuppressLint("UseSparseArrays")
    public ResourceLoaderCachedWrapper(ResourceLoader impl) {
        this.impl = impl;
    }

    public ResourceLoaderCachedWrapper() {
    }

    public void setImpl(ResourceLoader impl) {
        this.impl = impl;
    }

    @Override
    public void start() {
        impl.start();
    }

    @Override
    public int getPagesCount() {
        return impl.getPagesCount();
    }

    @Override
    public void loadPage(int page, OnPageLoadedListener loadListener) {
        if(cash.containsKey(page)) {
            loadListener.onPageLoaded(cash.get(page));
        } else {
            impl.loadPage(page, new OnPageLoadListenerWrapper(loadListener, page));
        }
//        impl.loadPage(page, loadListener);
    }

    @Override
    public Uri getResourceUri() {
        return impl.getResourceUri();
    }

    @Override
    public void close() throws Exception {
        impl.close();
    }

    public void clearCash(){
        cash.clear();
    }

    private class OnPageLoadListenerWrapper implements OnPageLoadedListener{

        private OnPageLoadedListener target;
        private int page;

        public OnPageLoadListenerWrapper(OnPageLoadedListener target, int page) {
            this.target = target;
            this.page = page;
        }

        @Override
        public void onPageLoaded(List<String> content) {
            cash.put(page, content);
            target.onPageLoaded(content);
        }
    }
}
