package com.bignerdranch.android.testpdfreader.model.storage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class PermissionManager {

    Context mContext;

    public PermissionManager(Context context) {
        mContext = context;
    }

    public void grantPermissions(Uri uri) {
        mContext.grantUriPermission(mContext.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
}
