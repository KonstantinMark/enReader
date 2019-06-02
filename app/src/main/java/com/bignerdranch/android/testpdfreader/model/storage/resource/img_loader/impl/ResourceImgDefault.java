package com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.impl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.ResourceImgLoader;

public class ResourceImgDefault implements ResourceImgLoader {

    private Context mContext;

    public ResourceImgDefault(Context context){
        mContext = context;
    }

    @Override
    public void setImage(Resource resource) {
        resource.drawable  = loadImg(resource);
    }

    @Override
    public Drawable loadImg(Resource resource) {
        return mContext.getResources().getDrawable(R.drawable.ic_undefined_black_24dp);
    }
}
