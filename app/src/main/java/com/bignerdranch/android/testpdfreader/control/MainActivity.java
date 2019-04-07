package com.bignerdranch.android.testpdfreader.control;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.model.MessageSchower;
import com.bignerdranch.android.testpdfreader.model.storage.BookStorage;

public class MainActivity extends AppCompatActivity {
    public static final String FILE_TYPE_PDF = "application/pdf";
    public static final String FILE_TYPE_TEXT = "text/plain";

    private static final int READ_REQUEST_CODE = 42;

    private Fragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragmentIfNeeded();

        setAddResourceBtnListener(findViewById(R.id.addResourceBtn));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if(resultData == null) return;

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = resultData.getData();
            BookStorage bookStorage = BookStorage.instance(getApplicationContext());
            if(!bookStorage.contains(uri)) {
                BookStorage.instance(getApplicationContext()).addPdfUri(uri);
            } else {
                MessageSchower.schow(findViewById(R.id.activity_main_root_element),
                        R.string.book_already_added, MessageSchower.DEFAULT);
            }
            notifyResourceItemAdded();
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

    private void notifyResourceItemAdded(){
        if(mFragment instanceof ResourceItemAddedListener){
            ((ResourceItemAddedListener) mFragment).resourceItemAdded();

        }
    }

    public interface ResourceItemAddedListener {
        void resourceItemAdded();
    }
}
