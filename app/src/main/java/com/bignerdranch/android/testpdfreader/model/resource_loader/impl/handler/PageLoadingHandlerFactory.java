package com.bignerdranch.android.testpdfreader.model.resource_loader.impl.handler;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.PageLoadingHandler;
import com.bignerdranch.android.testpdfreader.model.storage.Storage;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;

import java.io.IOException;

public class PageLoadingHandlerFactory {
    private Context mContext;

    public PageLoadingHandlerFactory(Context context) {
        mContext = context;
    }

    public <T extends OnPageLoadedListener> PageLoadingHandler<T>
    newInstance(Uri uri, Handler handler, PageLoadingHandler.OnLoadedListener<T> listener) throws IOException {
        Storage storage = Storage.instance(mContext);
        Resource resource = storage.get(uri);
        if (resource != null) {
            switch (resource.getType()) {
                case PDF:
                    return new PageLoadingHandlerPDF<T>(handler, listener, mContext, uri);
                case TXT:
                    return new PageLoadingHandlerTXT<>(handler, listener, mContext, uri);
                case DOCX:
                    return new PageLoadingHandlerWord<>(handler, listener, mContext, uri);
                default:
                    return null;
            }
        } else return null;
    }
}
