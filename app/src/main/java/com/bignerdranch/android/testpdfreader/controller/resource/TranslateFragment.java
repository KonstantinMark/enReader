package com.bignerdranch.android.testpdfreader.controller.resource;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.databinding.FragmentTranslateBinding;
import com.bignerdranch.android.testpdfreader.model.tools.OnCancelListener;
import com.bignerdranch.android.testpdfreader.model.tools.OnTranslationNotShowedListener;
import com.bignerdranch.android.testpdfreader.model.translator.WordTranslateListener;
import com.bignerdranch.android.testpdfreader.model.word.Word;
import com.bignerdranch.android.testpdfreader.view.TranslationFragmentViewModal;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

// TODO word: ничего не отображает по нажатию на слово "letters"
public class TranslateFragment extends Fragment implements WordTranslateListener {

    private static final String TAG = "ChapterListFragmentTXT";

    FragmentTranslateBinding mBinding;
    private TranslationFragmentViewModal mViewModal;
    private OnCancelListener mCancelListener;
    private OnTranslationNotShowedListener mNotShowedListener;

    public TranslateFragment() {
        mViewModal = new TranslationFragmentViewModal();
    }

    public static TranslateFragment newInstance(OnCancelListener listener) {
        TranslateFragment fragment = new TranslateFragment();
        fragment.mCancelListener = listener;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil
                .inflate(inflater, R.layout.
                                fragment_translate,
                        container, false);

        mBinding.setViewModel(mViewModal);
        mBinding.translateClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCancelListener.onCanceled();
            }
        });

        return mBinding.getRoot();
    }

    @Override
    public void onPause() {
        changTranslationRemovedListener(null);
        super.onPause();
    }

    public void showLoadingAnimation() {
        mViewModal.reset();
        changTranslationRemovedListener(null);
    }

    public void changTranslationRemovedListener(OnTranslationNotShowedListener listener) {
        if (mNotShowedListener != null) {
            if (mNotShowedListener == listener) return;
            mNotShowedListener.onNotShoved();
        }
        mNotShowedListener = listener;
    }

    @Override
    public void translateIsDone(Word word) {
        mViewModal.showTranslation(word);
    }

    @Override
    public void translateError(Error error) {
        mViewModal.showTranslationError(error.getMessage());
    }
}

