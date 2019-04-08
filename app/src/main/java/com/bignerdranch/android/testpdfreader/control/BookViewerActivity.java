package com.bignerdranch.android.testpdfreader.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.control.content.ITextSelectedReceiver;
import com.bignerdranch.android.testpdfreader.control.content.TranslationReceiver;
import com.bignerdranch.android.testpdfreader.model.ResourceDescriptor;
import com.bignerdranch.android.testpdfreader.model.ViewFragmentFactory;

public class BookViewerActivity extends AppCompatActivity implements ITextSelectedReceiver {
    private static final String EXTRA_RESOURCE_DESCRIPTOR =
            "com.bignerdranch.android.testpdfreader.model.ResourceDescriptor.resource_descriptor";

    public static Intent newIntent(Context packageContext, ResourceDescriptor descriptor){
        Intent intent = new Intent(packageContext, BookViewerActivity.class);
        intent.putExtra(EXTRA_RESOURCE_DESCRIPTOR, descriptor);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_viewer);

        FragmentManager fm = getSupportFragmentManager();
        Fragment contentFragment = fm.findFragmentById(R.id.fragment_container_content);

        if (contentFragment == null) {
            ResourceDescriptor descriptor = (ResourceDescriptor)
                    getIntent().getSerializableExtra(EXTRA_RESOURCE_DESCRIPTOR);

            contentFragment = ViewFragmentFactory.getFragment(descriptor);

            fm.beginTransaction()
                    .add(R.id.fragment_container_content, contentFragment)
                    .commit();
        }
    }

    @Override
    public void textSelected(String text) {
        Toast.makeText(this, "textSelected: " + text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void paragraphSelected(String paragraph, TranslationReceiver receiver) {
        Toast.makeText(this, "paragraphSelected: " + paragraph, Toast.LENGTH_SHORT).show();
        receiver.receiveTranslation("response ...");
    }
}
