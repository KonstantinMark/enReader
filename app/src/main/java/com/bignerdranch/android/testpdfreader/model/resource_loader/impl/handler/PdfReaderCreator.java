package com.bignerdranch.android.testpdfreader.model.resource_loader.impl.handler;

import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.itextpdf.text.pdf.PdfReader;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

public class PdfReaderCreator {
    private Context mContext;
    private Uri mUri;
    private PdfReader mPdfReader;

    public PdfReaderCreator(Context context, Uri uri) {
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

        FileDescriptor fileDescriptor = Objects.requireNonNull(parcelFileDescriptor).getFileDescriptor();
        FileInputStream stream = new FileInputStream(fileDescriptor);

        return new PdfReader(stream);
    }
}
