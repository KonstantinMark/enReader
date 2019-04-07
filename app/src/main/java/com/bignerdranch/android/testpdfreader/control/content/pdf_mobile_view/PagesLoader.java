package com.bignerdranch.android.testpdfreader.control.content.pdf_mobile_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class PagesLoader<T> extends HandlerThread {
    private static final String TAG = "PagesLoader";
    private static final int MESSAGE_LOAD = 0;

    private PdfReader mReader;
    private boolean mHasQuit = false;
    private Handler mRequestHandler;
    private ConcurrentMap<T, Integer> mRequestMap = new ConcurrentHashMap<>();
    private PageLoadedListener mListener;


    public interface PageLoadedListener <T>{
        void onPageLoaded(T target, String content);
    }

    public void setListener(PageLoadedListener listener) {
        mListener = listener;
    }

    public PagesLoader(Handler handler,  PdfReader reader) {
        super(TAG);
        mRequestHandler = handler;
        mReader = reader;

    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    @SuppressLint("HandlerLeak")
    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == MESSAGE_LOAD) {
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got a request for page: " + mRequestMap.get(target));
                    handleRequest(target, mRequestMap.get(target));
                    mRequestMap.remove(target);
                }
            }
        };
    }

    public void queueThumbnail(T target, Integer i){
        Log.i(TAG, "Got a page num: " + i);

        if(i == null){
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, i);
            mRequestHandler.obtainMessage(MESSAGE_LOAD, target)
                    .sendToTarget();
        }
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_LOAD);
        mRequestMap.clear();
    }


    private void handleRequest(final T target, Integer ind) {
        String result;
        int page = mRequestMap.get(target) + 1;
        try {
            PdfReaderContentParser parser = new PdfReaderContentParser(mReader);
            TextExtractionStrategy strategy =
                    parser.processContent(
                            page,
                            new SimpleTextExtractionStrategy());
            result = strategy.getResultantText();
        } catch (IOException e) {
            e.printStackTrace();
            result = e.toString();
        }

        mListener.onPageLoaded(target, result);
        mRequestMap.remove(target);
    }


}
