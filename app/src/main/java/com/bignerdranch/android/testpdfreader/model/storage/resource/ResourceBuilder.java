package com.bignerdranch.android.testpdfreader.model.storage.resource;

import android.content.Context;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.model.storage.resource.tool.ResourceInformation;

public class ResourceBuilder {


    public static Resource buildNew(Uri uri, Context context) {
        ResourceInformation mResourceInformation = new ResourceInformation(context);
        Resource resource = new Resource();
        resource.uri = uri;
        resource.name = mResourceInformation.getResourceName(uri);
        resource.type = mResourceInformation.identifyType(uri);

//        new ResourceImgFactory(mContext).getResourceImg(resource.getType()).setImage(resource);

//        resource.setMetaData(new MetaDataImpl());

//        MetaDataManager metaDataManager = new MetaDataManager();
//        metaDataManager.setLastOpenedDateCurrent(resource);
//        metaDataManager.setLastCurrentPageInformation(resource, 0, 0);

        return resource;
    }

}
