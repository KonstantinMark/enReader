package com.bignerdranch.android.testpdfreader.control;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.model.storage.FilePermissionManager;
import com.bignerdranch.android.testpdfreader.model.storage.Storage;
import com.bignerdranch.android.testpdfreader.model.storage.resource.ResourceBuilder;
import com.bignerdranch.android.testpdfreader.model.storage.resource.tool.ResourceInformation;
import com.bignerdranch.android.testpdfreader.model.storage.resource.tool.ResourceType;
import com.bignerdranch.android.testpdfreader.model.tools.MessageShower;

public class ResourceCreator {
    private static String TAG = "ResourceCreator";
    public interface Callback{
        void resourceInsertError(String message);

        void resourceAdded(Resource resource);
    }

    public static void newResourceQuery(Uri uri, Context context, Callback callback){
        AppDatabase db = AppDatabase.getInMemoryDatabase(context);

        Log.i(TAG, String.valueOf(db.resourceDao().loadResourceSync(uri) == null));
        // TODO сделать метод contains(..)
        if (db.resourceDao().loadResourceSync(uri) == null) {
            FilePermissionManager.grantPermissions(uri, context);
            Resource resource = ResourceBuilder.buildNew(uri, context);

            if(resource.getType() != ResourceType.UNDEFINE){
                db.resourceDao().insert(resource);
                callback.resourceAdded(resource);
            } else {
                String extension = new ResourceInformation(context)
                        .getResourceExpansion(resource.getUri());
                String message = context.getResources()
                        .getString(R.string.resource_type_undefined) + " " + extension;

                callback.resourceInsertError(message);
            }

        } else {
            callback.resourceInsertError(context.getResources()
                    .getString(R.string.book_already_added));
        }
    }

}
