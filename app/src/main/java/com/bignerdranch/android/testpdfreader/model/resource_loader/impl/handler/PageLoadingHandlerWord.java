package com.bignerdranch.android.testpdfreader.model.resource_loader.impl.handler;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.ParcelFileDescriptor;

import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.PageLoadingHandler;
import com.bignerdranch.android.testpdfreader.model.text.ParagraphNormalisator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PageLoadingHandlerWord<T extends OnPageLoadedListener> extends PageLoadingHandler<T> {

    private Context mContext;
    private List<List<String>> content = new ArrayList<>();

    public PageLoadingHandlerWord(Handler handler, OnLoadedListener<T> listener, Context context, Uri uri) throws IOException {
        super(handler, listener);
        mContext = context;

       loadBook(uri);
    }

    private void loadBook(Uri uri) throws FileNotFoundException {
        ParcelFileDescriptor parcelFileDescriptor =
                mContext.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        FileInputStream stream = new FileInputStream(fileDescriptor);

        try {
            XWPFDocument xdoc = new XWPFDocument(OPCPackage.open(stream));
            List<XWPFParagraph> paragraphList = xdoc.getParagraphs();

            List<String> page = new ArrayList<>();
            content.add(page);

            for (XWPFParagraph paragraph : paragraphList) {
                String tmp[] = paragraph.getText().split("\n");
                page.addAll(Arrays.asList(tmp));
                if(page.size() >= 10) {
                    page = new ArrayList<>();
                    content.add(page);
                }
            }
//            while (scanner.hasNextLine()){
//                page.addAll(ParagraphNormalisator.normalise(scanner.nextLine()));
//                if(page.size() >= 10) {
//                    page = new ArrayList<>();
//                    content.add(page);
//                }
//            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }

//        try {
//
//            HWPFDocument xdoc = new HWPFDocument(stream);
//            WordExtractor we = new WordExtractor(xdoc);
//            String[] paragraphs = we.getParagraphText();
//
//            List<String> page = new ArrayList<>();
//            content.add(page);
//
//            for (String para : paragraphs) {
//                page.add(para);
//                if(page.size() >= 10) {
//                    page = new ArrayList<>();
//                    content.add(page);
//                }
//            }
//            stream.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void handleRequest(final T target, Integer page) {
        onLoaded(target, content.get(page));
    }

    @Override
    protected int getPageCount() {
        return content.size();
//        return 5;
    }


}
