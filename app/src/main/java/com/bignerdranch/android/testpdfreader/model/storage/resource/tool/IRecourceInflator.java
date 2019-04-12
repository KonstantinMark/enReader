package com.bignerdranch.android.testpdfreader.model.storage.resource.tool;

import android.net.Uri;

import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;

public interface IRecourceInflator {
    void inflate(IResource resource, Uri uri);
}
