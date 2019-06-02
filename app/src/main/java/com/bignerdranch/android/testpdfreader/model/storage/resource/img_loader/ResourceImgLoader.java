package com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.bignerdranch.android.testpdfreader.db.entry.Resource;

public interface ResourceImgLoader {
    void setImage(Resource resource);

    Drawable loadImg(Resource resource);
}
