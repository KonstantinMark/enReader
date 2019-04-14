package com.bignerdranch.android.testpdfreader.view;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class FragmentPdfMobileViewViewModal extends BaseObservable {
    private Integer mFullPageCount;
    private int mCurrentPageNumber;

    @Bindable
    public int getFullPageCount() {
        return mFullPageCount != null ? mFullPageCount : 0;
    }

    public void setFullPageCount(int fullPageCount) {
        mFullPageCount = fullPageCount;
        notifyChange();
    }

    @Bindable
    public int getCurrentPageNumber() {
        return mCurrentPageNumber;
    }

    public void setCurrentPageNumber(int currentPageNumber) {
        mCurrentPageNumber = currentPageNumber;
        notifyChange();
    }

    @Bindable
    public int getLoadingProgressBarVisibility() {
        return mFullPageCount != null ? View.GONE : View.VISIBLE;
    }

    @Bindable
    public String getPageInformation() {
        return getCurrentPageNumber() + "/" + getFullPageCount();
    }
}
