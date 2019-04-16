package com.bignerdranch.android.testpdfreader.ui.resources_list;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.db.entry.MetaData;
import com.bignerdranch.android.testpdfreader.ui.resource.ResourceViewerActivity;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.model.storage.resource.MetaDataManager;

public class OnResourceClickListener implements View.OnClickListener {

    private Context mContext;
    private Resource mResource;

    public OnResourceClickListener(Context context,Resource resource) {
        mContext = context;
        mResource = resource;
    }

    @Override
    public void onClick(View v) {
        setResourceDateOpenedNow();
        Intent i = ResourceViewerActivity.newIntent(
                mContext,
                mResource
        );
        mContext.startActivity(i);
    }

    private void setResourceDateOpenedNow() {
        Runnable runnable = () -> {
            AppDatabase db = AppDatabase.getDatabase(mContext);
            MetaData metaData = db.metaDataDao().loadMetaDateSync(mResource.uri);

            MetaDataManager.setLastOpenedDateCurrent(metaData);

            db.metaDataDao().insert(metaData);
        };
        AsyncTask.execute(runnable);
    }
}
