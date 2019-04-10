package com.bignerdranch.android.testpdfreader.control;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.control.content.ICloseTranslationFragmentListener;
import com.bignerdranch.android.testpdfreader.control.content.ITextSelectedReceiver;
import com.bignerdranch.android.testpdfreader.control.content.ResourceReceiverFragment;
import com.bignerdranch.android.testpdfreader.control.content.TranslationReceiver;
import com.bignerdranch.android.testpdfreader.model.ResourceDescriptor;
import com.bignerdranch.android.testpdfreader.model.ViewFragmentFactory;
import com.bignerdranch.android.testpdfreader.model.translator.ParagraphTranslateListener;
import com.bignerdranch.android.testpdfreader.model.translator.Translator;
import com.bignerdranch.android.testpdfreader.model.translator.TranslatorFactory;
import com.bignerdranch.android.testpdfreader.model.translator.WordTranslateListener;
import com.bignerdranch.android.testpdfreader.model.word.Word;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BookViewerActivity extends AbstractActivityWithPermissions implements ITextSelectedReceiver, ICloseTranslationFragmentListener {
    private static final String EXTRA_RESOURCE_DESCRIPTOR =
            "com.bignerdranch.android.testpdfreader.model.ResourceDescriptor.resource_descriptor";

    private Translator mTranslator;
    private TranslateFragment mTranslateFragment;
    private ResourceReceiverFragment mContentFragment;

    public static Intent newIntent(Context packageContext, ResourceDescriptor descriptor){
        Intent intent = new Intent(packageContext, BookViewerActivity.class);
        intent.putExtra(EXTRA_RESOURCE_DESCRIPTOR, descriptor);
        return intent;
    }

    public BookViewerActivity() {
        TranslatorFactory translatorFactory = new TranslatorFactory();
        mTranslator = translatorFactory.newInstance();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_viewer);

        FragmentManager fm = getSupportFragmentManager();
        mContentFragment = (ResourceReceiverFragment)
                fm.findFragmentById(R.id.fragment_container_content);

        if (mContentFragment == null) {
            ResourceDescriptor descriptor = (ResourceDescriptor)
                    getIntent().getSerializableExtra(EXTRA_RESOURCE_DESCRIPTOR);

            ResourceReceiverFragment fragment = ViewFragmentFactory.getFragment(descriptor);

            fm.beginTransaction()
                    .add(R.id.fragment_container_content, fragment)
                    .commit();

            mContentFragment = fragment;
        }
    }

    @Override
    public void textSelected(String text) {
        removeFragmentTranslation();
        mTranslateFragment = TranslateFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.translate_show_down, R.anim.translate_show_up);
        fragmentTransaction.add(R.id.fragment_container_translator, mTranslateFragment);
        fragmentTransaction.commit();

        mTranslator.translateWord(text, new TranslationListener(), getApplicationContext());
    }

    @Override
    public void paragraphSelected(String paragraph, TranslationReceiver receiver) {
        onUserActionPerformed();
        mTranslator.translatePhrase(paragraph,
                new ParagraphTranslationListenerAdapter(receiver),
                getApplicationContext());
    }

    @Override
    public void onUserActionPerformed() {
        removeFragmentTranslation();
    }

    void removeFragmentTranslation() {
        if (mTranslateFragment != null) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.translate_hide_down, R.anim.translate_hide_up);
            fragmentTransaction.remove(mTranslateFragment).commit();
            mTranslateFragment = null;
            notifyRemoveTranslation();
        }
    }

    public void notifyRemoveTranslation() {
        mContentFragment.resetView();
    }

    private class ParagraphTranslationListenerAdapter implements ParagraphTranslateListener {

        private TranslationReceiver mReceiver;

        public ParagraphTranslationListenerAdapter(TranslationReceiver receiver) {
            mReceiver = receiver;
        }

        @Override
        public void translateIsDone(String paragraph) {
            mReceiver.receiveTranslation(paragraph);
        }

        @Override
        public void translateError(Error error) {
            mReceiver.receiveTranslation(error.toString());
        }
    }

    private class TranslationListener implements WordTranslateListener {
        @Override
        public void translateError(Error error) {
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void translateIsDone(Word word) {
            if (mTranslateFragment != null)
                mTranslateFragment.showWordTranslation(word);

        }

        // TODO not work after rotation, mTranslateFragment == null
    }
}
