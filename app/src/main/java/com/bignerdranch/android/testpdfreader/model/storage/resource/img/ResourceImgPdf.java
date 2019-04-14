package com.bignerdranch.android.testpdfreader.model.storage.resource.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

public class ResourceImgPdf implements IResourceImg {

    private Context mContext;

    public ResourceImgPdf(Context context) {
        mContext = context;
    }

    @Override
    public void setImage(IResource resource) {
        Bitmap bitmap = null;
            try (ParcelFileDescriptor descriptor =
                         mContext.getContentResolver().openFileDescriptor(resource.getUri(), "r");
                 PdfRenderer renderer = new PdfRenderer(Objects.requireNonNull(descriptor));
                 PdfRenderer.Page page = renderer.openPage(0)
            ) {

                bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);

                bitmap.eraseColor(0xFFFFFFFF);

                page.render(bitmap, null, null,
                        PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        resource.setImg(new BitmapDrawable(bitmap));
    }
}
