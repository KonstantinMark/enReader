package com.bignerdranch.android.testpdfreader.model.storage.resource;

import android.content.Context;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.model.storage.db.BookCursorWrapper;
import com.bignerdranch.android.testpdfreader.model.storage.resource.img.ResourceImgFactory;
import com.bignerdranch.android.testpdfreader.model.storage.resource.tool.ResourceInformation;

public class ResourceBuilder {

    private Context mContext;
    private ResourceInformation mResourceInformation;

    public ResourceBuilder(Context context) {
        mContext = context;
        mResourceInformation = new ResourceInformation(mContext);
    }

    public IResource buildNew(Uri uri) {
        Resource resource = new Resource();
        resource.setUri(uri);
        resource.setName(mResourceInformation.getResourceName(uri));
        resource.setType(mResourceInformation.identifyType(uri));
        new ResourceImgFactory(mContext).getResourceImg(resource.getType()).setImage(resource);

        MetaData metaData = new MetaData();
        resource.setMetaData(metaData);

        MetaDataManager metaDataManager = new MetaDataManager();
        metaDataManager.setLastOpenedDateCurrent(resource);

        return resource;
    }

    public IResource build(BookCursorWrapper cursor) {
        Resource resource = new Resource();
        MetaData metaData = new MetaData();
        resource.setMetaData(metaData);

        resource.setUri(cursor.getUri());
        resource.setType(cursor.getType());
        resource.setName(mResourceInformation.getResourceName(resource.getUri()));
        new ResourceImgFactory(mContext).getResourceImg(resource.getType()).setImage(resource);

        metaData.setTimeLastOpened(cursor.getTime());
        metaData.setState(cursor.getState());

        return resource;
    }

}
