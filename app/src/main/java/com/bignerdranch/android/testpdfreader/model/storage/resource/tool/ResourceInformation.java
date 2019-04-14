package com.bignerdranch.android.testpdfreader.model.storage.resource.tool;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

public class ResourceInformation {

    private Context mContext;

    public ResourceInformation(Context context) {
        mContext = context;
    }

    public ResourceType identifyType(Uri uri) {
        String expansion = getResourceExpansion(uri);
        ResourceType type;
        try {
            type = ResourceType.valueOf(expansion.toUpperCase());
        } catch (IllegalArgumentException e) {
            type = ResourceType.UNDEFINE;
        }

        return type;
    }

    public String getResourceExpansion(Uri uri) {
        try (Cursor cursor = mContext.getContentResolver()
                .query(uri, null, null, null, null, null)) {
            String name = null;
            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                if (name.lastIndexOf(".") != -1) {
                    name = name.substring(name.lastIndexOf('.') + 1);
                }
            }
            return name != null ? name.toUpperCase() : null;
        }
    }

    public String getResourceName(Uri uri) {
        Cursor cursor = mContext.getContentResolver()
                .query(uri, null, null, null, null, null);

        String name = "undefine";
        try {
            if (cursor != null && cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                if (name.lastIndexOf(".") != -1) {
                    name = name.substring(0, name.lastIndexOf('.'));
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return name;
    }

}
