package com.bignerdranch.android.testpdfreader.control;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.control.main_fragment.MainFragment;
import com.bignerdranch.android.testpdfreader.databinding.ActivityMainBinding;
import com.bignerdranch.android.testpdfreader.model.MessageSchower;
import com.bignerdranch.android.testpdfreader.model.storage.BookStorage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {
    public static final String FILE_TYPE_PDF = "application/pdf";
    public static final String FILE_TYPE_TEXT = "text/plain";

    private static final int READ_REQUEST_CODE = 42;

    private Fragment mFragment;
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
            Uri uri = resultData.getData();
            BookStorage bookStorage = BookStorage.instance(getApplicationContext());
            if (!bookStorage.contains(uri)) {
                BookStorage.instance(getApplicationContext()).addPdfUri(uri);
                notifyResourceItemAdded(uri);
            } else {
                MessageSchower.schow(mBinding.getRoot(),
                        R.string.book_already_added, MessageSchower.DEFAULT);
            }

        }
    }

    private void setFragmentIfNeeded(){
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if(f == null){
            f = MainFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
        }
        mFragment = f;
    }

    private void setAddResourceBtnListener(View v){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = createOnReadIntent(FILE_TYPE_PDF);
                startActivityForResult(i, READ_REQUEST_CODE);
            }
        });
    }

    private static Intent createOnReadIntent(String type){
        return new Intent(Intent.ACTION_OPEN_DOCUMENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType(type)
                .setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                        | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
    }

    private void notifyResourceItemAdded(Uri uri) {
        if(mFragment instanceof ResourceItemAddedListener){
            ((ResourceItemAddedListener) mFragment).notifyItemAdded(uri);

        }
    }

    public interface ResourceItemAddedListener {
        void notifyItemAdded(Uri uri);
    }
}
