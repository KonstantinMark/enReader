package com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.impl;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader.ResourceImgLoader;

public class ResourceImgDocxLoader implements ResourceImgLoader {

    private Context context;

    public ResourceImgDocxLoader(Context context) {
        this.context = context;
    }

    @Override
    public void setImage(Resource resource) {
        resource.drawable = loadImg(resource);

    }

    @Override
    public Drawable loadImg(Resource resource) {
        return context.getResources().getDrawable(R.drawable.word);
    }
}
