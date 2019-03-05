package com.bignerdranch.android.testpdfreader.model.storage.resource.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;

public class ResourceImgPdf implements IResourceImg {

    private Uri mUri;
    private Context mContext;
    private Bitmap mBitmap;

    public ResourceImgPdf(Uri uri, Context context){
        mUri = uri;
        mContext = context;
        mBitmap = null;
    }

    @Override
    public Bitmap get() {
        if(mBitmap == null) {
            try (ParcelFileDescriptor descriptor =
                         mContext.getContentResolver().openFileDescriptor(mUri, "r");
                 PdfRenderer renderer = new PdfRenderer(descriptor);
                 PdfRenderer.Page page = renderer.openPage(0);
            ) {

                mBitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);

                mBitmap.eraseColor(0xFFFFFFFF);

                page.render(mBitmap, null, null,
                        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return mBitmap;
    }
}
