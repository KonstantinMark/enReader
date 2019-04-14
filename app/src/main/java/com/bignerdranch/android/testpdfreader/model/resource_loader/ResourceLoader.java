package com.bignerdranch.android.testpdfreader.model.resource_loader;

public interface ResourceLoader extends AutoCloseable {

    int getPagesCount();

    void loadPage(int page, OnPageLoadedListener loadListener);
}
