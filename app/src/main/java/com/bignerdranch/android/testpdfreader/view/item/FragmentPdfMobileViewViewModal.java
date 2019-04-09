package com.bignerdranch.android.testpdfreader.view.item;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class FragmentPdfMobileViewViewModal extends BaseObservable {
    private int mFullPageCount;
    private int mCurrentPageNumber;

    @Bindable
    public int getFullPageCount() {
        return mFullPageCount;
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
    public String getPageInformation() {
        return getCurrentPageNumber() + "/" + getFullPageCount();
    }
}
