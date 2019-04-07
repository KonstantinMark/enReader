package com.bignerdranch.android.testpdfreader.view.item;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;

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

}
