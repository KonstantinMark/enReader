package com.bignerdranch.android.testpdfreader.view.item;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

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
