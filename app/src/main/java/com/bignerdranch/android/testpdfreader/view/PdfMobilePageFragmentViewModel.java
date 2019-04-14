package com.bignerdranch.android.testpdfreader.view;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class PdfMobilePageFragmentViewModel extends BaseObservable {

    private boolean mAnimationVisibility = true;

    public void setAnimationVisibility(boolean visibility) {
        this.mAnimationVisibility = visibility;
        notifyChange();
    }

    @Bindable
    public int getVisibilityLoadAnimation() {
        return mAnimationVisibility ? View.VISIBLE : View.GONE;
    }

}
