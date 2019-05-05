package com.bignerdranch.android.testpdfreader.controller.resource;

import android.net.Uri;
import android.os.Bundle;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class ResourceViewFragment extends Fragment {
    private static final String ARG_URL = "com.bignerdranch.android.testpdfreader.control.content_uri";

    private Uri mResourceUri;

    public static ResourceViewFragment config(Uri resourceUri, ResourceViewFragment fragment) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, resourceUri.toString());
        fragment.setArguments(args);
        return fragment;
    }

    public void resetView() {
        // DEFAULT ACTION - IGNORE
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(ARG_URL, mResourceUri.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null)
            mResourceUri = Uri.parse(savedInstanceState.getString(ARG_URL));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mResourceUri = Uri.parse(Objects.requireNonNull(getArguments()).getString(ARG_URL));
    }

    protected Uri getResourceUri() {
        return mResourceUri;
    }

}
