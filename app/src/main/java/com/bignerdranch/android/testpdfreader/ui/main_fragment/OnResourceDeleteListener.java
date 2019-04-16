package com.bignerdranch.android.testpdfreader.ui.main_fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.model.storage.Storage;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.google.android.material.snackbar.Snackbar;

public class OnResourceDeleteListener implements View.OnClickListener {

    private Context mContext;
    private MainFragment.ResourceAdapter mAdapter;
    private int mPosition;
    private Resource mResource;
    private View mView;

    public OnResourceDeleteListener(Context context, MainFragment.ResourceAdapter adapter, int position, Resource resource, View view) {
        mContext = context;
        mAdapter = adapter;
        mPosition = position;
        mResource = resource;
        mView = view;
    }

    @Override
    public void onClick(View v) {
//        mAdapter.removeItem(mPosition);

        Runnable runnable = () -> {
            AppDatabase db = AppDatabase.getDatabase(mContext);
            db.resourceDao().delete(mResource);
            showRemoveCancelMessage(mResource, mPosition);
        };
        AsyncTask.execute(runnable);
    }

    private void showRemoveCancelMessage(Resource resource, int position) {
        String title = mContext.getResources().getString(R.string.delete_undo_title);
        String btnTitle = mContext.getResources().getString(R.string.delete_undo_btn);

        Snackbar.make(mView, title, Snackbar.LENGTH_LONG)
                .setAction(btnTitle, new RemoveCancelListener(resource, position)).show();
    }

    private class RemoveCancelListener implements View.OnClickListener {
        private Resource resource;
        private int position;

        public RemoveCancelListener(Resource resource, int position) {
            this.resource = resource;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            Runnable runnable1 = () -> {
                Storage storage = Storage.instance(mContext);
                storage.insert(resource);
            };
            AsyncTask.execute(runnable1);

            Handler handler = new Handler(Looper.getMainLooper());
            Runnable runnable = () -> mAdapter.addItem(resource, position);
            handler.postDelayed(runnable, 0);
        }
    }
}
