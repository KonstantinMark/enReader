package com.bignerdranch.android.testpdfreader.view.item;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class PdfMobilePageFragmentViewModel extends BaseObservable {

    private Boolean contentReceived;

    public Boolean getContentReceived() {
        return contentReceived;
    }

    public void setContentReceived(Boolean contentReceived) {
        this.contentReceived = contentReceived;
        notifyChange();
    }

    @Bindable
    public int getVisibilityLoadAnimation() {
        return getContentReceived() != null ? View.GONE : View.VISIBLE;
    }

}
