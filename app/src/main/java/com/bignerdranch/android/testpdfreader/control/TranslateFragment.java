package com.bignerdranch.android.testpdfreader.control;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bignerdranch.android.testpdfreader.R;
import com.bignerdranch.android.testpdfreader.control.content.ICloseTranslationFragmentListener;
import com.bignerdranch.android.testpdfreader.databinding.FragmentTranslateBinding;
import com.bignerdranch.android.testpdfreader.model.word.Word;
import com.bignerdranch.android.testpdfreader.model.word.WordTool;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

// TODO word: ничего не отображает по нажатию на слово "letters"
public class TranslateFragment extends Fragment {

    private static final String TAG = "ChapterListFragmentTXT";
    private static final String ARG_WORD = "chapter_id";

    private TextView mWordTextView;
    private TextView mTranscriptionTextView;
    private TextView mTranslationTextView;
    private ProgressBar mLoadTranslateAnimation;

    public static TranslateFragment newInstance() {
        TranslateFragment fragment = new TranslateFragment();
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentTranslateBinding binding = DataBindingUtil
                .inflate(inflater, R.layout.
                                fragment_translate,
                        container, false);


        mWordTextView = binding.tWord;
        mTranscriptionTextView = binding.tTranscription;
        mTranslationTextView = binding.tTranslation;
        mLoadTranslateAnimation = binding.loadTranslateAnimation;

        binding.translateClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ICloseTranslationFragmentListener) getContext()).onUserActionPerformed();
            }
        });

        return binding.getRoot();
    }

    public void showWordTranslation(Word word) {
        mLoadTranslateAnimation.setVisibility(View.GONE);

        mWordTextView.setText(word.getWord());

        String transcription = word.getTranscription();

        WordTool wordTool = new WordTool(word);
        String translation = wordTool.translationToStringLine();
        mTranslationTextView.setText(translation);

        if (transcription != null) {
            transcription = "[" + word.getTranscription() + "]";
            mTranscriptionTextView.setText(transcription);

        } else {
            if (word.getTranslations().size() == 0) {
                mTranslationTextView.setText(R.string.word_not_found);
            }
        }
    }

}

