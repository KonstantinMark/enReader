package com.bignerdranch.android.testpdfreader.model.resource_loader.impl;

public interface PageLoadedListener<T> {
    void onPageLoaded(T target, String content);
}
