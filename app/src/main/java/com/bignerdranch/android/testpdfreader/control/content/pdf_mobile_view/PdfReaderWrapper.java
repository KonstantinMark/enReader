package com.bignerdranch.android.testpdfreader.control.content.pdf_mobile_view;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;

public class PdfReaderWrapper {
    private Context mContext;
    private Uri mUri;
    private PdfReader mPdfReader;

    public PdfReaderWrapper(Context context, Uri uri) {
        mContext = context;
        mUri = uri;
    }

    public PdfReader getPdfReader() throws IOException {
        if(mPdfReader == null){
            mPdfReader = getPgfReader(mUri);
        }
        return mPdfReader;
    }

    private PdfReader getPgfReader(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                mContext.getContentResolver().openFileDescriptor(uri, "r");

        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        FileInputStream stream = new FileInputStream(fileDescriptor);

        return new PdfReader(stream);
    }
}
