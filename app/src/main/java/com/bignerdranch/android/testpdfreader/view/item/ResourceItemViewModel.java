package com.bignerdranch.android.testpdfreader.view.item;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bignerdranch.android.testpdfreader.db.entry.Resource;

import androidx.databinding.BaseObservable;
import androidx.databinding.BindingAdapter;

public class ResourceItemViewModel extends BaseObservable {
    private Context mContext;
    private Resource mResource;

    public ResourceItemViewModel(Context context) {
        mContext = context;
    }

    public void setResource(Resource resource){
        mResource = resource;
    }

    // TODO
    public Drawable getImg(){
//        return mResource.getImg();
        return null;
    }

    public String getName(){
        return mResource.getName();
    }

    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

}
