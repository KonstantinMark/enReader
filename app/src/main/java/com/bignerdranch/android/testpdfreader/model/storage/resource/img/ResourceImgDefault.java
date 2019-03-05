package com.bignerdranch.android.testpdfreader.model.storage.resource.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.bignerdranch.android.testpdfreader.R;

public class ResourceImgDefault implements IResourceImg {

    private Context mContext;

    public ResourceImgDefault(Context context){
        mContext = context;
    }

    @Override
    public Bitmap get() {
        Drawable d = mContext.getResources().getDrawable(R.drawable.undefine_file);
        return ((BitmapDrawable) d).getBitmap();
    }
}
