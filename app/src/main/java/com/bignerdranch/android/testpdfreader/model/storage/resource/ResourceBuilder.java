package com.bignerdranch.android.testpdfreader.model.storage.resource;

import android.content.Context;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.model.storage.db.sqLite.BookCursorWrapper;
import com.bignerdranch.android.testpdfreader.model.storage.resource.img.ResourceImgFactory;
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

    public Resource build(BookCursorWrapper cursor) {
//        ResourceImpl resource = new ResourceImpl();
//        MetaDataImpl metaData = new MetaDataImpl();
//        resource.setMetaData(metaData);
//
//        resource.setUri(cursor.getUri());
//        resource.setType(cursor.getType());
//        resource.setName(mResourceInformation.getResourceName(resource.getUri()));
//        new ResourceImgFactory(mContext).getResourceImg(resource.getType()).setImage(resource);
//
//        metaData.setTimeLastOpened(cursor.getTime());
//        metaData.setCurrentPage(cursor.getCurrentPage());
//        metaData.setItemOnPage(cursor.getScrollOnPage());
//
//        return resource;
        return null;
    }

}
