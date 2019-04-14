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


    private Handler mRequestHandler;
    private ConcurrentMap<T, Integer> mRequestMap = new ConcurrentHashMap<>();

    public PageLoadingHandler(Handler handler) {
        super(TAG);
        mRequestHandler = handler;
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

    protected void onLoaded(final T target, final List<String> result) {
        removeRequest(target);
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                target.onLoaded(result);
            }
        };
        handler.postDelayed(runnable, 0);
    }

    protected abstract void handleRequest(final T target, Integer index);

    protected abstract int getPageCount();
}
