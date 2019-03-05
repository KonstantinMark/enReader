package com.bignerdranch.android.testpdfreader.model.storage.resource;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;

public class ResourceBuilder {

    public static IResource build(Uri uri, Context context){
        ResourceInformation information = new ResourceInformation(uri, context);
        String name = information.getResourceName();
        Bitmap img = information.getResourceImg();
        return new Resource(uri, name, new BitmapDrawable(img), information.getType());
    }

}
