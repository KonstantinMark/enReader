package com.bignerdranch.android.testpdfreader.ui.resource;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.ui.AbstractActivityWithPermissions;
import com.bignerdranch.android.testpdfreader.ui.resource.text_selection.ITextSelectionListener;
import com.bignerdranch.android.testpdfreader.ui.resource.text_selection.TextSelector;
import com.bignerdranch.android.testpdfreader.model.tools.ViewFragmentFactory;
import com.bignerdranch.android.testpdfreader.model.storage.Storage;
import com.bignerdranch.android.testpdfreader.db.entry.Resource;
import com.bignerdranch.android.testpdfreader.model.tools.OnTranslationNotShowedListenerAdapter;
import com.bignerdranch.android.testpdfreader.model.translator.OnParagraphTranslatedListener;
import com.bignerdranch.android.testpdfreader.model.translator.Translator;
import com.bignerdranch.android.testpdfreader.model.translator.TranslatorFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ResourceViewerActivity extends AbstractActivityWithPermissions
        implements ITextSelectionListener, SelectionRemovedListener {
    private static final String EXTRA_RESOURCE_URI =
            "com.bignerdranch.android.testpdfreader.model.ResourceDescriptor.resource_uri";

    private Translator mTranslator;
    private TranslateFragment mTranslateFragment;
    private ResourceViewFragment mContentFragment;
    private Uri mResourceUri;

    public ResourceViewerActivity() {
        Log.i("MY_TAG", "ResourceViewerActivity");
        TranslatorFactory translatorFactory = new TranslatorFactory();
        mTranslator = translatorFactory.newInstance();
    }

    public static Intent newIntent(Context packageContext, Resource resource) {
        Log.i("MY_TAG", "newIntent");
        Intent intent = new Intent(packageContext, ResourceViewerActivity.class);
        intent.putExtra(EXTRA_RESOURCE_URI, resource.getUri().toString());
        return intent;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString(EXTRA_RESOURCE_URI, mResourceUri.toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            mResourceUri = Uri.parse(savedInstanceState.getString(EXTRA_RESOURCE_URI));
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("MY_TAG", "onCreate");
        setContentView(R.layout.activity_book_viewer);

        mResourceUri = Uri.parse(getIntent().getStringExtra(EXTRA_RESOURCE_URI));

        FragmentManager fm = getSupportFragmentManager();

        if (!containsResourceViewFragment(fm)) {
            setResourceViewFragment(fm);
        }
        Log.i("MY_TAG", "onCreate...");
    }

    private boolean containsResourceViewFragment(FragmentManager fm) {
        mContentFragment = (ResourceViewFragment)
                fm.findFragmentById(R.id.fragment_container_content);
        return mContentFragment != null;
    }

    private void setResourceViewFragment(FragmentManager fm) {
        Log.i("MY_TAG", "setResourceViewFragment");
        Storage storage = Storage.instance(getApplicationContext());
        Resource resource = storage.get(mResourceUri);

        ResourceViewFragment fragment = ViewFragmentFactory.getFragment(resource);

        fm.beginTransaction()
                .add(R.id.fragment_container_content, fragment)
                .commit();

        mContentFragment = fragment;
        Log.i("MY_TAG", "setResourceViewFragment...");
    }

    @Override
    public void wordSelected(String text, TextSelector listener) {

        if (mTranslateFragment == null) {
            createFragmentTranslation();
        } else {
            mTranslateFragment.reset();
        }
        mTranslateFragment.changTranslationNotShowedListener(
                new OnTranslationNotShowedListenerAdapter(listener));

        mTranslator.translateWord(text, mTranslateFragment, getApplicationContext());
        listener.select();
    }

    @Override
    public void paragraphSelected(String paragraph, OnParagraphTranslatedListener receiver) {
        removeFragmentTranslation();
        mTranslator.translatePhrase(paragraph, receiver, getApplicationContext());
    }

    @Override
    public void allUnSelected() {
        removeFragmentTranslation();
    }

    void createFragmentTranslation() {
        mTranslateFragment = TranslateFragment.newInstance(this::removeFragmentTranslation);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.translate_show_down, R.anim.translate_show_up);
        fragmentTransaction.add(R.id.fragment_container_translator, mTranslateFragment);
        fragmentTransaction.commit();
    }

    void removeFragmentTranslation() {
        if (mTranslateFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.translate_hide_down, R.anim.translate_hide_up);
            fragmentTransaction.remove(mTranslateFragment).commit();
            mTranslateFragment = null;
        }
    }

    @Override
    public void onSelectionRemoved() {
        removeFragmentTranslation();
    }
}
