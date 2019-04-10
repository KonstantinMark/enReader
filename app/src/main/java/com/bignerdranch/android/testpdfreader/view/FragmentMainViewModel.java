package com.bignerdranch.android.testpdfreader.view;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class FragmentMainViewModel extends BaseObservable {

    private boolean mProgressBarVisibility = false;

    @Bindable
    public int getProgressBarVisibility() {
        return mProgressBarVisibility ? View.VISIBLE : View.GONE;
    }

    public void setProgressBarVisibility(boolean progressBarVisibility) {
        mProgressBarVisibility = progressBarVisibility;
        notifyChange();
    }

}
