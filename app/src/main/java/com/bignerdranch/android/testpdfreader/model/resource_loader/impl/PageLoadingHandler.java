package com.bignerdranch.android.testpdfreader.model.resource_loader.impl;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class PageLoadingHandler<T extends OnPageLoadedListener> extends HandlerThread {
    private static final String TAG = "PageLoadingHandler";
    private static final int MESSAGE_LOAD = 0;

    public interface OnLoadedListener<T>{
        void onLoaded(T target, final List<String> result);
    }

    private Handler mRequestHandler;
    private ConcurrentMap<T, Integer> mRequestMap = new ConcurrentHashMap<>();
    private OnLoadedListener<T> mLoadedListener;

    public PageLoadingHandler(Handler handler, OnLoadedListener<T> listener) {
        super(TAG);
        mRequestHandler = handler;
        mLoadedListener = listener;
    }

    protected void onLoaded(T target, final List<String> result){
        if(mLoadedListener != null)
            mLoadedListener.onLoaded(target, result);
    }

    protected Integer getRequest(T target) {
        return mRequestMap.get(target);
    }

    protected void removeRequest(T target) {
        mRequestMap.remove(target);
    }

    protected void addRequest(T target, Integer ind) {
        mRequestMap.put(target, ind);
    }

    @Override
    public boolean quit() {
        return super.quit();
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_LOAD) {
                    T target = (T) msg.obj;
                    if(target.dataStillNeeded())
                        handleRequest(target, getRequest(target));
                    mRequestMap.remove(target);
                }
            }
        };
    }

    public void addToQueue(T target, Integer i) {
        if (i == null) {
            removeRequest(target);
        } else {
            addRequest(target, i);
            mRequestHandler.obtainMessage(MESSAGE_LOAD, target)
                    .sendToTarget();
        }
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_LOAD);
        mRequestMap.clear();
    }

    protected abstract void handleRequest(final T target, Integer index);

    protected abstract int getPageCount();
}
