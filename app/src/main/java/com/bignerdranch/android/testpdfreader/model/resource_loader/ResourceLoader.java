package com.bignerdranch.android.testpdfreader.model.resource_loader;

import android.net.Uri;

public interface ResourceLoader extends AutoCloseable {

    void start();

    int getPagesCount();

    void loadPage(int page, OnPageLoadedListener loadListener);

    Uri getResourceUri();
}
