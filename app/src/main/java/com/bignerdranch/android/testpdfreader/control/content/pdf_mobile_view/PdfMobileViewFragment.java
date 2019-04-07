package com.bignerdranch.android.testpdfreader.control.content.pdf_mobile_view;

import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.control.content.TextSelectorFragment;
import com.bignerdranch.android.testpdfreader.databinding.FragmentPdfMobileViewBinding;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;

import java.io.IOException;
import java.util.List;

public class PdfMobileViewFragment extends TextSelectorFragment {

    private static final String TAG = "PdfMobileViewFragment";
    private List<String> mPages;

    private FragmentPdfMobileViewBinding mBinding;
    private RecyclerView.Adapter mAdapter;

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
                        mBinding.fragmentPdfMobileViewPageNumber.setText(i + "/" + getCount());
                        return fragment;
                    }

                    @Override
                    public int getCount() {
                        return loader.getCount();
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
            Log.i(TAG, content);
            target.setContent(content);
        }
    }

}
