package com.bignerdranch.android.testpdfreader.control.resource.view_fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.control.resource.ResourceViewFragment;
import com.bignerdranch.android.testpdfreader.control.resource.SelectionRemovedListener;
import com.bignerdranch.android.testpdfreader.control.resource.view_fragment.mobile_view.PageFragment;
import com.bignerdranch.android.testpdfreader.databinding.FragmentPdfMobileViewBinding;
import com.bignerdranch.android.testpdfreader.model.resource_loader.ResourceLoader;
import com.bignerdranch.android.testpdfreader.model.resource_loader.impl.ResourceLoaderImpl;
import com.bignerdranch.android.testpdfreader.model.storage.Storage;
import com.bignerdranch.android.testpdfreader.model.storage.resource.IMetaData;
import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;
import com.bignerdranch.android.testpdfreader.model.tools.marker.Marker;
import com.bignerdranch.android.testpdfreader.model.tools.marker.MarkerManager;
import com.bignerdranch.android.testpdfreader.view.FragmentPdfMobileViewViewModal;
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
import androidx.viewpager.widget.ViewPager;

public class MobileViewFragment extends ResourceViewFragment {

    private static final String TAG = "MobileViewFragment";
    private SelectionRemovedListener mSelectionRemovedListener;

    private FragmentPdfMobileViewBinding mBinding;
    private FragmentPdfMobileViewViewModal mViewModal;
    private FragmentStatePagerAdapterImpl mFragmentStatePagerAdapter;
    private IResource mResource;
    private IMetaData mResourceMetaData;

    private MarkerManager mMarkerManager;
    private ResourceLoader mResourceLoader;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
        mSelectionRemovedListener = (SelectionRemovedListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSelectionRemovedListener = null;
    }

    @Override
    public void onPause() {
        Storage storage = Storage.instance(getContext());
        Marker marker = mMarkerManager.getCurrentMarker();
        if (marker != null) {
            updateResourceCurrentPage(marker.getPageIndex());
            updateResourceCurrentPageItem(marker.getPageElementIndex());
        }
        storage.update(mResource);
        super.onPause();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("MY_TAG", "Mobile_onCreateView");
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_pdf_mobile_view,
                        container, false);
        mViewModal = new FragmentPdfMobileViewViewModal();
        mBinding.setViewModel(mViewModal);

        mMarkerManager = new MarkerManager();

        Storage storage = Storage.instance(getContext());
        mResource = storage.get(getResourceUri());
        mResourceMetaData = mResource.getMetaData();

        final FragmentManager fragmentManager = Objects.requireNonNull(getActivity()).getSupportFragmentManager();
        mFragmentStatePagerAdapter = new FragmentStatePagerAdapterImpl(fragmentManager);
        mBinding.fragmentPdfMobileViewViewPager.setAdapter(mFragmentStatePagerAdapter);

        createResourceLoader();
        return mBinding.getRoot();
    }

    private void createResourceLoader() {
        Handler handler = new Handler();

        Runnable runnable = () ->{
                final ResourceLoader loader;
                try {
                    loader = new ResourceLoaderImpl(getResourceUri(), getContext(), handler);
                    loader.start();
                    new Handler(Looper.getMainLooper()).postDelayed(
                            () -> resourceLoaderCreated(loader), 0);

                } catch (final IOException e) {
                    e.printStackTrace();
                    new Handler(Looper.getMainLooper())
                            .postDelayed(() -> onResourceLoadingError(e), 0);
                }
            };
        AsyncTask.execute(runnable);
    }

    private void onResourceLoadingError(IOException e) {
        Snackbar.make(Objects.requireNonNull(getView()),
                getResources().getString(R.string.resource_loading_error)
                        + "\n" + e.toString(),
                Snackbar.LENGTH_LONG).show();
    }

    private void resourceLoaderCreated(ResourceLoader resourceLoader) {
        mFragmentStatePagerAdapter.setResourceLoader(resourceLoader);

        mViewModal.setFullPageCount(resourceLoader.getPagesCount());
        setCurrentPage(mResourceMetaData.getCurrentPage());
        mBinding.fragmentPdfMobileViewViewPager.addOnPageChangeListener(new OnPageChangeListenerImpl());
    }

    private void setCurrentPage(int currentPage) {
        mBinding.fragmentPdfMobileViewViewPager.setCurrentItem(currentPage);
    }

    private void currentPageChanged(int currentPagePosition) {
        mBinding.getViewModel().setCurrentPageNumber(currentPagePosition + 1);
        notifySelectionRemoved();
        mMarkerManager.removeCurrentMarker();
        updateResourceCurrentPage(currentPagePosition);
    }

    private void notifySelectionRemoved() {
        mSelectionRemovedListener.onSelectionRemoved();
    }

    private void updateResourceCurrentPage(int currentPage) {
        mResource.getMetaData().setCurrentPage(currentPage);
        updateResourceCurrentPageItem(0);
    }

    private void updateResourceCurrentPageItem(int currentPageItem) {
        mResource.getMetaData().setItemOnPage(currentPageItem);
    }

    private class OnPageChangeListenerImpl extends ViewPager.SimpleOnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            currentPageChanged(position);
        }
    }

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
            PageFragment fragment = PageFragment
                    .newInstance(position, getCurrentItemOnPage(position));
            fragment.setMarkerIntentListener(mMarkerManager.getIntentListener());

            if (mPageLoader != null) mPageLoader.loadPage(position, fragment);
            return fragment;
        }

        private int getCurrentItemOnPage(int pageNumber) {
            return mResourceMetaData.getCurrentPage() == pageNumber
                    ? mResourceMetaData.getItemOnPage()
                    : -1;
        }

        @Override
        public int getCount() {
            return mPageLoader != null ? mPageLoader.getPagesCount() : 0;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mResourceLoader!=null) {
            try {
                mResourceLoader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
