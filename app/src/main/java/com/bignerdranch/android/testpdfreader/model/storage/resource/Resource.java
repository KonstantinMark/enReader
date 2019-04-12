package com.bignerdranch.android.testpdfreader.model.storage.resource;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.bignerdranch.android.testpdfreader.model.storage.resource.tool.ResourceType;

import java.sql.Timestamp;
import java.util.Objects;

public class Resource implements IResource {
    private Uri mUri;
    private String mName;
    private Drawable mImg;
    private ResourceType mType;
    private IMetaData mMetaData;

    public Resource() {
    }

    public Resource(Uri uri) {
        mUri = uri;
    }

    @Override
    public Uri getUri() {
        return mUri;
    }

    public void setUri(Uri uri) {
        mUri = uri;
    }

    @Override
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public Drawable getImg() {
        return mImg;
    }

    public void setImg(Drawable img) {
        mImg = img;
    }

    @Override
    public ResourceType getType() {
        return mType;
    }

    public void setType(ResourceType type) {
        mType = type;
    }

    @Override
    public IMetaData getMetaData() {
        return mMetaData;
    }

    @Override
    public void setMetaData(IMetaData metaData) {
        mMetaData = metaData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Resource resource = (Resource) o;
        return Objects.equals(mUri, resource.mUri);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mUri);
    }

    @Override
    public int compareTo(IResource o) {
        if (o == null) return 1;
        Timestamp my = getMetaData() != null ? getMetaData().getTimeLastOpened() : null;
        Timestamp his = o.getMetaData() != null ? o.getMetaData().getTimeLastOpened() : null;
        if (my != null) return my.compareTo(his);
        if (his != null) return -1;
        return 0;
    }
}
