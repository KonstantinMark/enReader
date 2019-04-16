package com.bignerdranch.android.testpdfreader.model.storage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class FilePermissionManager {

    public static void grantPermissions(Uri uri, Context context) {
        context.grantUriPermission(context.getPackageName(), uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.getContentResolver().takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }
}
