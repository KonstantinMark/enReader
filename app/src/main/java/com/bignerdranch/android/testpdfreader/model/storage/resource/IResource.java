package com.bignerdranch.android.testpdfreader.model.storage.resource;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public interface IResource {

    String getName();

    Drawable getImg();

    Uri getUri();

    ResourceType getType();
}
