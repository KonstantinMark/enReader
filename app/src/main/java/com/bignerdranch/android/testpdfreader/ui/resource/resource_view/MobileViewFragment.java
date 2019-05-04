package com.bignerdranch.android.testpdfreader.ui.resource.resource_view;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.databinding.FragmentMobilePageListBinding;
import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.ui.resource.ResourceViewFragment;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.PageFragment;
import com.bignerdranch.android.testpdfreader.model.resource_loader.ResourceLoader;
import com.bignerdranch.android.testpdfreader.model.tools.marker.MarkerManager;
import com.bignerdranch.android.testpdfreader.model.TextTranslationAction;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.TextTranslationActionListener;
import com.bignerdranch.android.testpdfreader.view.FragmentPdfMobileViewViewModal;
import com.bignerdranch.android.testpdfreader.viewmodal.ResourceLoaderViewModel;
import com.bignerdranch.android.testpdfreader.viewmodal.ResourceViewModel;
import com.google.android.material.snackbar.Snackbar;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

public class MobileViewFragment extends ResourceViewFragment {

    private static final String TAG = "MobileViewFragment";
    private TextTranslationActionListener mTextTranslationActionListener;

    private FragmentMobilePageListBinding mBinding;
    private FragmentStatePagerAdapterImpl mFragmentStatePagerAdapter;
    private ResourceLoaderViewModel mLoaderViewModel;
    private ResourceViewModel mResourceViewModel;
    private AppDatabase db;
//    private MetaData mResourceMetaData;

    private MarkerManager mMarkerManager;
//    private ResourceLoader mResourceLoader;

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

    @Override
    public void onPause() {
//        Storage storage = Storage.instance(getContext());
//        Marker marker = mMarkerManager.getCurrentMarker();
//        if (marker != null) {
//            updateResourceCurrentPage(marker.getPageIndex());
//            updateResourceCurrentPageItem(marker.getPageElementIndex());
//        }
//        storage.update(mResource);
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_page_list, container, false);
        mBinding.setViewModel(new FragmentPdfMobileViewViewModal());

        db = AppDatabase.getDatabase(getContext());
//        mMarkerManager = new MarkerManager();

//        Storage storage = Storage.instance(getContext());
//        mResource = storage.get(getResourceUri());
//        mResourceMetaData = mResource.getMetaData();

        final FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        mFragmentStatePagerAdapter = new FragmentStatePagerAdapterImpl(fragmentManager);
        mBinding.fragmentPdfMobileViewViewPager.setAdapter(mFragmentStatePagerAdapter);

        mLoaderViewModel = ViewModelProviders.of(this).get(ResourceLoaderViewModel.class);
        mResourceViewModel = ViewModelProviders.of(this).get(ResourceViewModel.class);

        mLoaderViewModel.load(getResourceUri(), this::onResourceLoadingError);
        mResourceViewModel.load(getResourceUri());

        getResourceLoader();
        observeCurrentPageChangeListener();
//        createResourceLoader();
        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBinding.fragmentPdfMobileViewViewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener(){
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        AsyncTask.execute(()-> db.metaDataDao().update(getResourceUri(), position, 0));}
                });
    }

    private void getResourceLoader(){
        mLoaderViewModel.getResourceLoader().observe(this, resourceLoader -> {
            if(resourceLoader!= null){
                mFragmentStatePagerAdapter.setResourceLoader(resourceLoader);
                mBinding.getViewModel().setFullPageCount(resourceLoader.getPagesCount());
            }
        });
    }

    private void onResourceLoadingError(IOException e) {
        Snackbar.make(Objects.requireNonNull(getView()),
                getResources().getString(R.string.resource_loading_error)
                        + "\n" + e.toString(),
                Snackbar.LENGTH_LONG).show();
    }

    private void observeCurrentPageChangeListener(){
        mResourceViewModel.getMetaDate().observe(this, metaData -> {
            if(metaData!= null){
                mBinding.getViewModel().setCurrentPageNumber(metaData.currentPage);
                notifySelectionRemoved();
            }
        });
    }

//    private void currentPageChanged(int currentPagePosition) {
//        mBinding.getViewModel().setCurrentPageNumber(currentPagePosition + 1);
//        notifySelectionRemoved();
//        mMarkerManager.removeCurrentMarker();
//        updateResourceCurrentPage(currentPagePosition);
//    }

//    private void createResourceLoader() {
//        Handler handler = new Handler();
//
//        Runnable runnable = () -> {
//                final ResourceLoader loader;
//                try {
//                    loader = new ResourceLoaderImpl(getResourceUri(), getContext(), handler);
//                    loader.start();
//                    new Handler(Looper.getMainLooper()).postDelayed(
//                            () -> resourceLoaderCreated(loader), 0);
//
//                } catch (final IOException e) {
//                    e.printStackTrace();
//                    new Handler(Looper.getMainLooper())
//                            .postDelayed(() -> onResourceLoadingError(e), 0);
//                }
//            };
//        AsyncTask.execute(runnable);
//    }




//    private void setCurrentPage(int currentPage) {
//        mBinding.fragmentPdfMobileViewViewPager.setCurrentItem(currentPage);
//    }



    private void notifySelectionRemoved() {
        mTextTranslationActionListener.onNewAction(new TextTranslationAction(
                TextTranslationAction.ActionType.ACTION_UN_SELECT));
    }

//    private void updateResourceCurrentPage(int currentPage) {
//        mResource.getMetaData().setCurrentPage(currentPage);
//        updateResourceCurrentPageItem(0);
//    }

//    private void updateResourceCurrentPageItem(int currentPageItem) {
//        mResource.getMetaData().setItemOnPage(currentPageItem);
//    }


    private class FragmentStatePagerAdapterImpl extends FragmentStatePagerAdapter {
        private ResourceLoader mPageLoader;

        FragmentStatePagerAdapterImpl(@NonNull FragmentManager fm) {
            super(fm);
        }

        public void setResourceLoader(final ResourceLoader pageLoader) {
            mPageLoader = pageLoader;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            PageFragment fragment = PageFragment.newInstance(getResourceUri(), position);
//            fragment.setMarkerIntentListener(mMarkerManager.getIntentListener());
            if (mPageLoader != null) mPageLoader.loadPage(position, fragment);
            return fragment;
        }

//        private int getCurrentItemOnPage(int pageNumber) {
//            return mResourceMetaData.getCurrentPage() == pageNumber
//                    ? mResourceMetaData.getCurrentItem()
//                    : -1;
//        }

        @Override
        public int getCount() {
            return mPageLoader != null ? mPageLoader.getPagesCount() : 0;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if(mResourceLoader!=null) {
//            try {
//                mResourceLoader.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        mLoaderViewModel.close();
    }
}
