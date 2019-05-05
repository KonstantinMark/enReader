package com.bignerdranch.android.testpdfreader.controller.resource.resource_view.page;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.controller.resource.resource_view.service.impl.PageParagraphWrapper;
import com.bignerdranch.android.testpdfreader.databinding.FragmentMobilePageBinding;
import com.bignerdranch.android.testpdfreader.controller.resource.service.TextTranslationActionListener;
import com.bignerdranch.android.testpdfreader.model.resource_loader.OnPageLoadedListener;
import com.bignerdranch.android.testpdfreader.view.PdfMobilePageFragmentViewModel;
import com.bignerdranch.android.testpdfreader.viewmodal.ResourceDBViewModel;
import com.bignerdranch.android.testpdfreader.viewmodal.ResourceStorageLoaderViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

public class PageFragment extends Fragment
        implements OnPageLoadedListener {
    private static String TAG = "PageFragment";

    private static String ARG_URI = "com.bignerdranch.android.testpdfreader.ui.resource.view_fragment.mobile_view.uri";
    private static String ARG_PAGE_NUM = "com.bignerdranch.android.testpdfreader.ui.resource.view_fragment.mobile_view.page_num";
    private TextTranslationActionListener mITextSelectionListener;

    private FragmentMobilePageBinding mBinding;
    private PageAdapter mPageAdapter;

    private Uri resourceUri;
    private int pageNum;

    public static PageFragment newInstance(Uri resourceUri, int pageNum) {
        Bundle arg = new Bundle();
        arg.putString(ARG_URI, resourceUri.toString());
        arg.putInt(ARG_PAGE_NUM, pageNum);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_mobile_page,
                        container, false);
        mBinding.setViewModel(new PdfMobilePageFragmentViewModel());

        resourceUri = Uri.parse(getArguments().getString(ARG_URI));
        pageNum = getArguments().getInt(ARG_PAGE_NUM);

        mBinding.fragmentPdfMobilePageRecyclerView.setLayoutManager(
                new LinearLayoutManager(getActivity()));

        ResourceDBViewModel resourceDBViewModel = ViewModelProviders.of(this).get(ResourceDBViewModel.class);
        resourceDBViewModel.loadResource(resourceUri);


        mPageAdapter = new PageAdapter(mITextSelectionListener);
        mBinding.fragmentPdfMobilePageRecyclerView.setAdapter(mPageAdapter);


        ResourceStorageLoaderViewModel storageLoaderViewModel = ViewModelProviders.of(this).get(ResourceStorageLoaderViewModel.class);
        storageLoaderViewModel.getResourceLoader().observe(this, resourceLoader -> {
            if(resourceLoader!= null){
                resourceLoader.loadPage(pageNum, this);
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onPageLoaded(List<String> content) {
        setContent(content);
    }

    public void setContent(List<String> content) {
        List<PageParagraphWrapper> list = PageParagraphWrapper.wrap(content);
        mPageAdapter.setItemList(list);
        closeAnimation();
    }

    private void closeAnimation() {
        mBinding.getViewModel().setAnimationVisibility(false);
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(ARG_URI, resourceUri.toString());
        outState.putInt(ARG_PAGE_NUM, pageNum);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            resourceUri = Uri.parse(savedInstanceState.getString(ARG_URI));
            pageNum = savedInstanceState.getInt(ARG_PAGE_NUM);
        }
    }

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mITextSelectionListener = (TextTranslationActionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mITextSelectionListener = null;
    }

}
