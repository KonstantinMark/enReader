package com.bignerdranch.android.testpdfreader.model.storage.resource.img;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import com.bignerdranch.android.testpdfreader.model.storage.resource.ResourceType;

public class ResourceImgFactory {

    public static IResourceImg getResourceImg(Uri uri, Context context){
        ResourceType type = getType(uri, context);

        switch (type){
            case PDF: return new ResourceImgPdf(uri, context);
            default:return new ResourceImgDefault(context);
        }

    }

    public static ResourceType getType(Uri uri, Context context){
        String expansion = getResourceExpansion(uri, context);
        ResourceType type;
        try {
            type = ResourceType.valueOf(expansion.toUpperCase());
        } catch (IllegalArgumentException e){
            type = ResourceType.UNDEFINE;
        }

        return type;
    }

    public static String getResourceExpansion(Uri uri, Context context){
        Cursor cursor = context.getContentResolver()
                .query(uri, null, null, null, null, null);

        String name = null;
        try {
            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                if (name.lastIndexOf(".") != -1) {
                    name = name.substring(name.lastIndexOf('.') + 1);
                }
            }
        } finally {
            cursor.close();
        }

        return name;
    }

}
