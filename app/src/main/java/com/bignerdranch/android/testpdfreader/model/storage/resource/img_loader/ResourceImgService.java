package com.bignerdranch.android.testpdfreader.model.storage.resource.img_loader;

import android.content.Context;

import com.bignerdranch.android.testpdfreader.db.entry.Resource;

import java.util.List;

public class ResourceImgService {
    public static void loadImages(List<Resource> resources, Context context){
        ResourceImgFactory factory = new ResourceImgFactory(context);
        for (Resource r: resources)
            factory.getResourceImgLoader(r.type).setImage(r);
    }
}
