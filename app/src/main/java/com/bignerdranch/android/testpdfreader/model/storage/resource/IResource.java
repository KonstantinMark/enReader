package com.bignerdranch.android.testpdfreader.model.storage.resource;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.model.storage.resource.tool.ResourceType;

public interface IResource extends Comparable<IResource> {

    String getName();

    void setName(String name);

    Drawable getImg();

    void setImg(Drawable drawable);

    Uri getUri();

    void setUri(Uri uri);

    ResourceType getType();

    void setType(ResourceType type);

    IMetaData getMetaData();

    void setMetaData(IMetaData metaData);

}
