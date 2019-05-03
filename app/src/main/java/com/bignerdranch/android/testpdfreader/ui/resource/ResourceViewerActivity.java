package com.bignerdranch.android.testpdfreader.ui.resource;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.ui.AbstractActivityWithPermissions;
import com.bignerdranch.android.testpdfreader.ui.resource.text_selection.ITextSelectionListener;
import com.bignerdranch.android.testpdfreader.ui.resource.text_selection.TextSelector;
import com.bignerdranch.android.testpdfreader.model.tools.ViewFragmentFactory;
import com.bignerdranch.android.testpdfreader.model.tools.OnTranslationNotShowedListenerAdapter;
import com.bignerdranch.android.testpdfreader.model.translator.OnParagraphTranslatedListener;
import com.bignerdranch.android.testpdfreader.viewmodal.TranslationManagerViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class ResourceViewerActivity extends AbstractActivityWithPermissions
        implements ITextSelectionListener, SelectionRemovedListener {
    private static String TAG = "ResourceViewerActivity";

    private static final String EXTRA_RESOURCE_URI =
            "com.bignerdranch.android.testpdfreader.model.ResourceDescriptor.resource_uri";

    private TranslateFragment mTranslateFragment;
    private ResourceViewFragment mContentFragment;
    private Uri mResourceUri;
    private TranslationManagerViewModel mTranslationManagerViewModel;

    public static Intent newIntent(Context packageContext, Uri resourceUri) {
        Intent intent = new Intent(packageContext, ResourceViewerActivity.class);
        intent.putExtra(EXTRA_RESOURCE_URI, resourceUri.toString());
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

        mTranslationManagerViewModel = ViewModelProviders.of(this)
                .get(TranslationManagerViewModel.class);

        FragmentManager fm = getSupportFragmentManager();

        if (!containsResourceViewFragment(fm)) {
            setResourceViewFragment(fm);
        }
    }

    private boolean containsResourceViewFragment(FragmentManager fm) {
        mContentFragment = (ResourceViewFragment)
                fm.findFragmentById(R.id.fragment_container_content);
        return mContentFragment != null;
    }

    private void setResourceViewFragment(FragmentManager fm) {
        ResourceViewFragment fragment = ViewFragmentFactory.getFragment(mResourceUri);

        fm.beginTransaction()
                .add(R.id.fragment_container_content, fragment)
                .commit();
        mContentFragment = fragment;
    }

    @Override
    public void wordSelected(String text, TextSelector listener) {
        if (mTranslateFragment == null) {
            createFragmentTranslation();
        } else {
            mTranslateFragment.showAnimation();
        }
        mTranslateFragment.changTranslationNotShowedListener(
                new OnTranslationNotShowedListenerAdapter(listener));

        mTranslationManagerViewModel.getTranslator()
                .translateWord(text, mTranslateFragment, getApplicationContext());
        listener.select();
    }

    @Override
    public void paragraphSelected(String paragraph, OnParagraphTranslatedListener receiver) {
        removeFragmentTranslation();
        mTranslationManagerViewModel.getTranslator()
                .translatePhrase(paragraph, receiver, getApplicationContext());
    }

    @Override
    public void allUnSelected() {
        removeFragmentTranslation();
    }

    void createFragmentTranslation() {
        if(mTranslateFragment == null) {
            mTranslateFragment = TranslateFragment.newInstance(this::removeFragmentTranslation);
        }
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
