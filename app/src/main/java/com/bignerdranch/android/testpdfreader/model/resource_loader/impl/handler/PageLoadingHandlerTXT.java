package com.bignerdranch.android.testpdfreader.model.resource_loader.impl.handler;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.ParcelFileDescriptor;

import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.PageLoadingHandler;
import com.bignerdranch.android.testpdfreader.model.text.ParagraphNormalisator;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PageLoadingHandlerTXT<T extends OnPageLoadedListener> extends PageLoadingHandler<T> {

    private Context mContext;
    private List<List<String>> content = new ArrayList<>();

    public PageLoadingHandlerTXT(Handler handler, OnLoadedListener<T> listener, Context context, Uri uri) throws IOException {
        super(handler, listener);
        mContext = context;

       loadBook(uri);
    }

    private void loadBook(Uri uri) throws FileNotFoundException {
        ParcelFileDescriptor parcelFileDescriptor =
                mContext.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        FileInputStream stream = new FileInputStream(fileDescriptor);
        Scanner scanner = new Scanner(stream);

        List<String> page = new ArrayList<>();
        content.add(page);

        while (scanner.hasNextLine()){
            page.addAll(ParagraphNormalisator.normalise(scanner.nextLine()));
            if(page.size() >= 10) {
                page = new ArrayList<>();
                content.add(page);
            }
        }
    }

    @Override
    protected void handleRequest(final T target, Integer page) {
//        String result;
//        try {
//            PdfReaderContentParser parser = new PdfReaderContentParser(mReader);
//            TextExtractionStrategy strategy =
//                    parser.processContent(
//                            page + 1,
//                            new SimpleTextExtractionStrategy());
//            result = strategy.getResultantText();
//        } catch (IOException e) {
//            e.printStackTrace();
//            result = e.toString();
//        }
//
//        TextManager textManager = new TextManager(result);

        onLoaded(target, content.get(page));
    }

    @Override
    protected int getPageCount() {
        return content.size();
    }


}
