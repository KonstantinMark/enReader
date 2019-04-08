package com.bignerdranch.android.testpdfreader.control.content.pdf_mobile_view;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.control.content.ResourceReceiverFragment;
import com.bignerdranch.android.testpdfreader.databinding.FragmentPdfMobileViewBinding;
import com.itextpdf.text.pdf.PdfReader;

import java.io.IOException;

public class PdfMobileViewFragment extends ResourceReceiverFragment {

    private static final String TAG = "PdfMobileViewFragment";

    private FragmentPdfMobileViewBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_pdf_mobile_view,
                        container, false);

        Uri uri = getResourceUri();
        final PageFragmentManager loader = new PageFragmentManager(uri);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        mBinding.fragmentPdfMobileViewViewPager.setAdapter(
                new FragmentStatePagerAdapter(fragmentManager) {
                    @Override
                    public Fragment getItem(int i) {
                        PageFragment fragment = PageFragment.newInstance();
                        loader.load(i, fragment);
                        return fragment;
                    }

                    @Override
                    public int getCount() {
                        return loader.getCount();
                    }
                });

        mBinding.fragmentPdfMobileViewViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                mBinding.fragmentPdfMobileViewPageNumber.setText((i + 1) + "/" + loader.getCount());
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        return mBinding.getRoot();
    }


    private class PageFragmentManager implements PagesLoader.PageLoadedListener<PageFragment>{
        private Uri mUri;
        private PdfReader mReader;
        PagesLoader<PageFragment> mPagesLoader;
        public PageFragmentManager(Uri uri) {
            mUri = uri;
            PdfReaderWrapper pdfReaderWrapper = new PdfReaderWrapper(getContext(), mUri);
            try {
                mReader = pdfReaderWrapper.getPdfReader();
                Handler handler = new Handler();
                mPagesLoader = new PagesLoader<>(handler, mReader);
                mPagesLoader.setListener(this);
                mPagesLoader.start();
                mPagesLoader.getLooper();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
        }

        public void close(){
            mPagesLoader.quit();
            mReader.close();
        }

        public int getCount(){
            return mReader != null ? mReader.getNumberOfPages() : 0;
        }

        public void load(int i, PageFragment fragment){
            mPagesLoader.queueThumbnail(fragment, i);
        }

        @Override
        public void onPageLoaded(PageFragment target, String content) {
            TextManager textManager = new TextManager(content);
            target.setContent(textManager.getParagraphs());
        }
    }

}
