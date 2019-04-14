package com.bignerdranch.android.testpdfreader.control.main_fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import com.bignerdranch.android.testpdfreader.control.resource.ResourceViewerActivity;
import com.bignerdranch.android.testpdfreader.model.storage.Storage;
import com.bignerdranch.android.testpdfreader.model.storage.resource.IResource;
import com.bignerdranch.android.testpdfreader.model.storage.resource.MetaDataManager;

public class OnResourceClickListener implements View.OnClickListener {

    private Context mContext;
    private IResource mResource;
    private int mPosition;
    private MainFragment.ResourceAdapter mAdapter;

    public OnResourceClickListener(Context context, MainFragment.ResourceAdapter adapter, int position, IResource resource) {
        mContext = context;
        mResource = resource;
        mPosition = position;
        mAdapter = adapter;
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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Storage storage = Storage.instance(mContext);
                IResource resource = storage.get(mResource.getUri());
                MetaDataManager metaDataManager = new MetaDataManager();
                metaDataManager.setLastOpenedDateCurrent(resource);
                storage.update(resource);
            }
        };
        AsyncTask.execute(runnable);

        mAdapter.moveItem(mPosition, 0);
    }
}
