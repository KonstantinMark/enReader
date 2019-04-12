package com.bignerdranch.android.testpdfreader.model.storage.resource.img;

import android.content.Context;

import com.bignerdranch.android.testpdfreader.model.storage.resource.tool.ResourceType;

public class ResourceImgFactory {

    private Context mContext;

    public ResourceImgFactory(Context context) {
        mContext = context;
    }

    public IResourceImg getResourceImg(ResourceType type) {
        switch (type){
            case PDF:
                return new ResourceImgPdf(mContext);
            default:
                return new ResourceImgDefault(mContext);
        }

    }

}
