package com.bignerdranch.android.testpdfreader.controller.resource.resource_view.service.impl;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.bignerdranch.android.testpdfreader.controller.resource.resource_view.page.PageFragment;
import com.bignerdranch.android.testpdfreader.model.resource_loader.ResourceLoader;

public class FragmentStatePagerAdapterImpl extends FragmentStatePagerAdapter {
    private Uri resourceUri;
    private int pageCount = 0;

    public FragmentStatePagerAdapterImpl(@NonNull FragmentManager fm, Uri resourceUri) {
        super(fm);
        this.resourceUri = resourceUri;
    }

    public void setPageCount(int pageCount){
        this.pageCount = pageCount;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        PageFragment fragment = PageFragment.newInstance(resourceUri, position);
        return fragment;
    }

    @Override
    public int getCount() {
        return pageCount;
    }
}