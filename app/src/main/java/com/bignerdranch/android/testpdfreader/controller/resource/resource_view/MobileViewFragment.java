package com.bignerdranch.android.testpdfreader.controller.resource.resource_view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.controller.resource.resource_view.service.impl.FragmentStatePagerAdapterImpl;
import com.bignerdranch.android.testpdfreader.databinding.FragmentMobilePageListBinding;
import com.bignerdranch.android.testpdfreader.controller.resource.ResourceViewFragment;
import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.model.TextTranslationAction;
import com.bignerdranch.android.testpdfreader.controller.resource.service.TextTranslationActionListener;
import com.bignerdranch.android.testpdfreader.view.FragmentPdfMobileViewViewModal;
import com.bignerdranch.android.testpdfreader.viewmodal.ResourceStorageLoaderViewModel;
import com.bignerdranch.android.testpdfreader.viewmodal.ResourceDBViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class MobileViewFragment extends ResourceViewFragment {

    private static final String TAG = "MobileViewFragment";
    private TextTranslationActionListener mTextTranslationActionListener;

    private FragmentMobilePageListBinding mBinding;

    private FragmentStatePagerAdapterImpl mFragmentStatePagerAdapter;

    private ResourceStorageLoaderViewModel mStorageLoaderViewModel;
    private ResourceDBViewModel mResourceDBViewModel;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mTextTranslationActionListener = (TextTranslationActionListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mTextTranslationActionListener = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_page_list, container, false);
        mBinding.setViewModel(new FragmentPdfMobileViewViewModal());

        final FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        mFragmentStatePagerAdapter = new FragmentStatePagerAdapterImpl(fragmentManager, getResourceUri());
        mBinding.fragmentPdfMobileViewViewPager.setAdapter(mFragmentStatePagerAdapter);

        mStorageLoaderViewModel = ViewModelProviders.of(this).get(ResourceStorageLoaderViewModel.class);

        mResourceDBViewModel = ViewModelProviders.of(this).get(ResourceDBViewModel.class);
        mResourceDBViewModel.loadResource(getResourceUri());

        startStorageResourceLoading();

        return mBinding.getRoot();
    }


    private boolean lastPageLoaded = false;
    private void loadLastPage(){
        mResourceDBViewModel.getMetaDate().observe(this, metaData -> {
            if(metaData!=null && !lastPageLoaded) {
                mResourceDBViewModel.getMetaDate().removeObservers(this);
                mBinding.fragmentPdfMobileViewViewPager.setCurrentItem(metaData.currentPage);
                lastPageLoaded = true;
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding.fragmentPdfMobileViewViewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener(){
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if(lastPageLoaded) {
                            AsyncTask.execute(() -> {
                                mResourceDBViewModel.updateCurrentPage(position);
                            });
                        }
                        mBinding.getViewModel().setCurrentPageNumber(position);
                        notifyCurrentPageChanged();
                    }
                });
    }



    private void startStorageResourceLoading(){
        Handler handler = new Handler();
        AsyncTask.execute(()->{
            try {
                mStorageLoaderViewModel.load(getResourceUri(), handler);
            } catch (IOException e) {
                onResourceLoadingError(e);
            }
        });
        observeResourceLoader();
    }

    private void observeResourceLoader(){
        mStorageLoaderViewModel.getResourceLoader().observe(this, resourceLoader -> {
            if(resourceLoader!= null){
                mFragmentStatePagerAdapter.setPageCount(resourceLoader.getPagesCount());
                loadLastPage();
                mBinding.getViewModel().setFullPageCount(resourceLoader.getPagesCount());
            }
        });
    }


    private void notifyCurrentPageChanged() {
        mTextTranslationActionListener.onNewAction(new TextTranslationAction(
                TextTranslationAction.ActionType.ACTION_CURRENT_PAGE_CHANGED));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mStorageLoaderViewModel.close();
    }

    private void onResourceLoadingError(IOException e) {
        Snackbar.make(Objects.requireNonNull(getView()),
                getResources().getString(R.string.resource_loading_error)
                        + "\n" + e.toString(),
                Snackbar.LENGTH_LONG).show();
    }

}
