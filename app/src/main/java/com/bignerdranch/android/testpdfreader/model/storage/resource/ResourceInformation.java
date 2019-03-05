package com.bignerdranch.android.testpdfreader.model.storage.resource;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.bignerdranch.android.testpdfreader.model.storage.resource.img.ResourceImgFactory;

public class ResourceInformation {

    private Uri mUri;
    private Context mContext;
    private ResourceType mType = null;

    public ResourceInformation(Uri uri, Context context){
        mContext = context;
        mUri = uri;
    }

    public Bitmap getResourceImg(){
        return ResourceImgFactory.getResourceImg(mUri, mContext).get();
    }

    public String getResourceName(){
        Cursor cursor = mContext.getContentResolver()
                .query(mUri, null, null, null, null, null);

        String name = "undefine";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                if (name.lastIndexOf(".") != -1) {
                    name = name.substring(0, name.lastIndexOf('.'));
                }
            }
        } finally {
            cursor.close();
        }

        return name;
    }

    public ResourceType getType(){
        if(mType == null){
            mType = ResourceImgFactory.getType(mUri, mContext);
        }
        return mType;
    }

}
