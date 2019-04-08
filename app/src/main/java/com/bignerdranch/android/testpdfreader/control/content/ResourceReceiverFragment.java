package com.bignerdranch.android.testpdfreader.control.content;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.bignerdranch.android.testpdfreader.control.content.pdf_mobile_view.PdfMobileViewFragment;

public class ResourceReceiverFragment extends Fragment {
    private static final String ARG_URL = "uri";

    private Uri mUri;

    public static ResourceReceiverFragment newInstance(String uri) {
        Bundle args = new Bundle();
        args.putString(ARG_URL, uri);

        PdfMobileViewFragment fragment = new PdfMobileViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUri = Uri.parse(getArguments().getString(ARG_URL));
    }

    protected Uri getResourceUri() {
        return mUri;
    }


}
