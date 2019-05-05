package com.bignerdranch.android.testpdfreader.model.resource_loader.impl.handler;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.PageLoadingHandler;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.PdfReaderCreator;
import com.bignerdranch.android.testpdfreader.model.text.TextManager;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

import java.io.IOException;

public class PageLoadingHandlerPDF<T extends OnPageLoadedListener> extends PageLoadingHandler<T> {

    private PdfReader mReader;
    private Context mContext;

    public PageLoadingHandlerPDF(Handler handler, OnLoadedListener<T> listener, Context context, Uri uri) throws IOException {
        super(handler, listener);
        mContext = context;
        PdfReaderCreator pdfReaderCreator = new PdfReaderCreator(mContext, uri);
        mReader = pdfReaderCreator.getPdfReader();
    }

    @Override
    protected void handleRequest(final T target, Integer page) {
        String result;
        try {
            PdfReaderContentParser parser = new PdfReaderContentParser(mReader);
            TextExtractionStrategy strategy =
                    parser.processContent(
                            page + 1,
                            new SimpleTextExtractionStrategy());
            result = strategy.getResultantText();
        } catch (IOException e) {
            e.printStackTrace();
            result = e.toString();
        }

        TextManager textManager = new TextManager(result);

        onLoaded(target, textManager.getParagraphs());
    }

    @Override
    protected int getPageCount() {
        return mReader != null ? mReader.getNumberOfPages() : 0;
    }


}
