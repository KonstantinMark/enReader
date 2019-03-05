package com.bignerdranch.android.testpdfreader.model.storage.resource;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class Resource implements IResource {
    private Uri mUri;
    private String mName;
    private Drawable mImg;
    private ResourceType mType;

    public Resource(Uri uri, String name, Drawable img, ResourceType type){
        mUri = uri;
        mName = name;
        mImg = img;
        mType = type;
    }


    @Override
    public String getName() {
        return mName;
    }

    @Override
    public Drawable getImg() {
        return mImg;
    }

    @Override
    public Uri getUri() {
        return mUri;
    }

    @Override
    public ResourceType getType() {
        return mType;
    }
}
