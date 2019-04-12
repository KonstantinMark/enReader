package com.bignerdranch.android.testpdfreader.model.storage.resource.img;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;

public class ResourceImgDefault implements IResourceImg {

    private Context mContext;

    public ResourceImgDefault(Context context){
        mContext = context;
    }

    @Override
    public void setImage(IResource resource) {
        Drawable d = mContext.getResources().getDrawable(R.drawable.undefine_file);
        resource.setImg(d);
    }
}
