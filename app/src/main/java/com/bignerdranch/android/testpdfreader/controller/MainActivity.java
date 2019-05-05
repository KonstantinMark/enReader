package com.bignerdranch.android.testpdfreader.controller;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.db.AppDatabase;
import com.bignerdranch.android.testpdfreader.db.entry.MetaData;
import com.bignerdranch.android.testpdfreader.model.storage.FilePermissionManager;
import com.bignerdranch.android.testpdfreader.model.storage.resource.MetaDataManager;
import com.bignerdranch.android.testpdfreader.model.storage.resource.ResourceBuilder;
import com.bignerdranch.android.testpdfreader.model.tools.MessageShower;
import com.bignerdranch.android.testpdfreader.controller.resources_list.ResourceListFragment;
import com.bignerdranch.android.testpdfreader.databinding.ActivityMainBinding;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    public static final String FILE_TYPE_PDF = "application/pdf";
    public static final String FILE_TYPE_TEXT = "text/plain";

    private static String TAG = "MainActivity";

    private static final int READ_REQUEST_CODE = 1;

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil
                .setContentView(this, R.layout.activity_main);

        setFragmentIfNeeded();

        setAddResourceBtnListener(mBinding.addResourceBtn);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultData == null) return;

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Runnable runnable = () -> {
                Uri uri = resultData.getData();
                AppDatabase db = AppDatabase.getDatabase(getApplicationContext());

                FilePermissionManager.grantPermissions(uri, getApplicationContext());
                Resource newResource = ResourceBuilder.buildNew(uri, getApplicationContext());
                try {
                    db.resourceDao().insert(newResource);
                    MetaData metaData = new MetaData();
                    metaData.uri = newResource.uri;
                    MetaDataManager.setLastOpenedDateCurrent(metaData);
                    db.metaDataDao().insert(metaData);
                } catch (SQLiteConstraintException exception){
                    MessageShower.show(mBinding.getRoot(), R.string.book_already_added,
                                MessageShower.DEFAULT);
                    exception.printStackTrace();
                }

            };
            AsyncTask.execute(runnable);
        }
    }

    private void setFragmentIfNeeded(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if(f == null){
            f = ResourceListFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }
    }

    private void setAddResourceBtnListener(View v){
        v.setOnClickListener(v1 -> {
            Intent i = createOnReadIntent(FILE_TYPE_PDF);
            startActivityForResult(i, READ_REQUEST_CODE);
        });
    }

    private static Intent createOnReadIntent(String type){
        return new Intent(Intent.ACTION_OPEN_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType(type)
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
    }
}
