package com.bignerdranch.android.testpdfreader.view.item;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class PdfMobileItemViewModel extends BaseObservable {
    private String TAG = "PdfMobileItemViewModel";
    private String mContent;

    public void setContent(String content){
        mContent = content;
        notifyChange();
    }

    @Bindable
    public String getContent(){
        return mContent;
    }

    @Bindable
    public int getVisibilityLoadAnimation() {
        return getContent() != null ? View.GONE : View.VISIBLE;
    }

}
