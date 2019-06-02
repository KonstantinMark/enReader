package com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.impl;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.ResourceImgLoader;

public class ResourceImgTxtLoader implements ResourceImgLoader {

    private Context context;

    public ResourceImgTxtLoader(Context context) {
        this.context = context;
    }

    @Override
    public void setImage(Resource resource) {
        resource.drawable = loadImg(resource);

    }

    @Override
    public Drawable loadImg(Resource resource) {
        return context.getResources().getDrawable(R.drawable.txt);
    }
}
