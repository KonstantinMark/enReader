package com.bignerdranch.android.testpdfreader.control.content.pdf_mobile_view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.control.content.ICloseTranslationFragmentListener;
import com.bignerdranch.android.testpdfreader.control.content.ResourceReceiverFragment;
import com.bignerdranch.android.testpdfreader.databinding.FragmentPdfMobileViewBinding;
import com.bignerdranch.android.testpdfreader.view.item.FragmentPdfMobileViewViewModal;
import com.itextpdf.text.pdf.PdfReader;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class PdfMobileViewFragment extends ResourceReceiverFragment {

    private static final String TAG = "PdfMobileViewFragment";
    private ICloseTranslationFragmentListener mUserNavigationActionListener;

    private FragmentPdfMobileViewBinding mBinding;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mUserNavigationActionListener = (ICloseTranslationFragmentListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_pdf_mobile_view,
                        container, false);

        final Uri uri = getResourceUri();
        mBinding.setViewModel(new FragmentPdfMobileViewViewModal());
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentStatePagerAdapterImpl impl = new FragmentStatePagerAdapterImpl(fragmentManager);
        mBinding.fragmentPdfMobileViewViewPager.setAdapter(impl);
        mBinding.fragmentPdfMobileViewViewPager.addOnPageChangeListener(new OnPageChangeListenerImpl());

        @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                PageFragmentManager manager = new PageFragmentManager(uri);
                impl.setManager(manager);
                mBinding.getViewModel().setFullPageCount(manager.getCount());
                mBinding.getViewModel().setCurrentPageNumber(1);
                return null;
            }
        };

        asyncTask.execute();

        return mBinding.getRoot();
    }

    private class FragmentStatePagerAdapterImpl extends FragmentStatePagerAdapter {

        private PageFragmentManager mManager;

        public FragmentStatePagerAdapterImpl(@NonNull FragmentManager fm) {
            super(fm);
        }

        public void setManager(final PageFragmentManager manager) {


            final Handler handler = new Handler(Looper.getMainLooper());
            final Runnable changeView = new Runnable() {
                public void run() {
                    mManager = manager;
                    notifyDataSetChanged();
                }
            };
            handler.postDelayed(changeView, 0);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            PageFragment fragment = PageFragment.newInstance();
            if (mManager != null) mManager.load(position, fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            return mManager != null ? mManager.getCount() : 0;
        }
    }

    private class OnPageChangeListenerImpl implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            mBinding.getViewModel().setCurrentPageNumber(position + 1);
            notifyUserNavigationActionListener();
        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mUserNavigationActionListener = null;
    }

    private void notifyUserNavigationActionListener() {
        if (mUserNavigationActionListener != null) {
            mUserNavigationActionListener.onUserActionPerformed();
        }
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
                Handler handler = new Handler(Looper.getMainLooper());
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
