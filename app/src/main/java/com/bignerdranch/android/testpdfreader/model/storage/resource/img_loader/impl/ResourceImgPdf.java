package com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.ResourceImgLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class ResourceImgPdf implements ResourceImgLoader {

    private Context mContext;

    public ResourceImgPdf(Context context) {
        mContext = context;
    }

    @Override
    public void setImage(Resource resource) {
            // TODO
        resource.drawable = loadImg(resource);
            if(resource.drawable == null){
                resource.drawable = mContext.getResources().getDrawable(R.drawable.pdf);
            }
    }

    @Override
    public Drawable loadImg(Resource resource) {
        return  null;
//        Bitmap bitmap = null;
//        try (ParcelFileDescriptor descriptor =
//                     mContext.getContentResolver().openFileDescriptor(resource.getUri(), "r");
//             PdfRenderer renderer = new PdfRenderer(Objects.requireNonNull(descriptor));
//             PdfRenderer.Page page = renderer.openPage(0)
//        ) {
//
//            bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
//
//            bitmap.eraseColor(0xFFFFFFFF);
//
//            page.render(bitmap, null, null,
//                    PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
////        return bitmap;
//        return null;
    }
}
