package com.bignerdranch.android.testpdfreader.ui.resource;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.model.tools.OnTranslationRemovedListenerAdapter;
import com.bignerdranch.android.testpdfreader.ui.AbstractActivityWithPermissions;
import com.bignerdranch.android.testpdfreader.model.TextTranslationAction;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.TextTranslationActionListener;
import com.bignerdranch.android.testpdfreader.model.tools.ViewFragmentFactory;
import com.bignerdranch.android.testpdfreader.viewmodal.TranslationManagerViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

public class ResourceViewerActivity extends AbstractActivityWithPermissions
        implements TextTranslationActionListener {
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

//    @Override
//    public void wordSelected(String text, TextSelector listener) {
//        if (mTranslateFragment == null) {
//            createFragmentTranslation();
//        } else {
//            mTranslateFragment.showLoadingAnimation();
//        }
//        mTranslateFragment.changTranslationRemovedListener(
//                new OnTranslationRemovedListenerAdapter(listener));
//
//        mTranslationManagerViewModel.getTranslator()
//                .translateWord(text, mTranslateFragment, getApplicationContext());
//        listener.select();
//    }

//    @Override
//    public void paragraphSelected(String paragraph, TranslationListener receiver) {
//        removeFragmentTranslation();
//        mTranslationManagerViewModel.getTranslator()
//                .translatePhrase(paragraph, receiver, getApplicationContext());
//    }

//    @Override
//    public void allUnSelected() {
//        removeFragmentTranslation();
//    }

    @Override
    public void onNewAction(TextTranslationAction action) {
        switch (action.getActionType()){
            case ACTION_WORD_SELECTED:
                if (mTranslateFragment == null) {
                    createFragmentTranslation();
                } else {
                    mTranslateFragment.showLoadingAnimation();
                }
                mTranslateFragment.changTranslationRemovedListener(
                        new OnTranslationRemovedListenerAdapter(action.getTextSelector()));

                mTranslationManagerViewModel.getTranslator()
                        .translateWord(action.getText(), mTranslateFragment, getApplicationContext());
                action.getTextSelector().select();
                break;
            case ACTION_PARAGRAPH_SELECTED:
            case ACTION_PART_PARAGRAPH_SELECTED:
                removeFragmentTranslation();
                mTranslationManagerViewModel.getTranslator()
                        .translatePhrase(action.getText(), action.getTranslationListener(),
                                getApplicationContext());
                break;
            case ACTION_UN_SELECT:
                removeFragmentTranslation();
                break;
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



//    @Override
//    public void onSelectionRemoved() {
//        removeFragmentTranslation();
//    }

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
}
