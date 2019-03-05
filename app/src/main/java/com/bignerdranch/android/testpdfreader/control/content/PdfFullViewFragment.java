package com.bignerdranch.android.testpdfreader.control.content;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.databinding.FragmentPdfFullViewBinding;

public class PdfFullViewFragment extends TextSelectorFragment {
    private static final String TAG = "PdfJsViewFragment";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentPdfFullViewBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_pdf_full_view,
                        container, false);

        binding.fragmentPdfFullViewPdfView.fromUri(getResourceUri()).load();

        return binding.getRoot();
    }

}
