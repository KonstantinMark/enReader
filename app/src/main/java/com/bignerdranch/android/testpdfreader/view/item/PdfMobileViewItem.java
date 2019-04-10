package com.bignerdranch.android.testpdfreader.view.item;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class PdfMobileViewItem extends BaseObservable {
    private String TAG = "PdfMobileViewItem";
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
