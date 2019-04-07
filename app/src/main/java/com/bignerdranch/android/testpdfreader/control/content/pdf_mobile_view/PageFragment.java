package com.bignerdranch.android.testpdfreader.control.content.pdf_mobile_view;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.control.content.TextSelectorFragment;
import com.bignerdranch.android.testpdfreader.databinding.ListItemPdfMobileViewBinding;
import com.bignerdranch.android.testpdfreader.view.item.PdfMobileViewItem;

public class PageFragment extends Fragment {
     private String TAG = "PageFragment";

    private ListItemPdfMobileViewBinding mBinding;
    private PdfMobileViewItem mPdfMobileViewItem;

    public static PageFragment newInstance(){
        PageFragment fragment = new PageFragment();
        return fragment;
    }

    @SuppressLint("ValidFragment")
    private PageFragment(){
        mPdfMobileViewItem = new PdfMobileViewItem();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.list_item__pdf_mobile_view,
                        container, false);

        mBinding.setViewModel(mPdfMobileViewItem);

        return mBinding.getRoot();
    }

    public void setContent(String content){
        TextManager textManager = new TextManager(content);
        mPdfMobileViewItem.setContent(textManager.getNormalisedText());
    }
}
