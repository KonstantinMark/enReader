package com.bignerdranch.android.testpdfreader.view;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;

public class ItemResourceViewModel extends BaseObservable {
    private Context mContext;
    private IResource mResource;

    public ItemResourceViewModel(Context context){
        mContext = context;
    }

    public void setResource(IResource resource){
        mResource = resource;
    }

    public Drawable getImg(){
        return mResource.getImg();
    }

    public String getName(){
        return mResource.getName();
    }

    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

}
