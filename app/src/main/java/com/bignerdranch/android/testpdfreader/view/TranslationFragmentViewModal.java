package com.bignerdranch.android.testpdfreader.view;

import android.view.View;

import com.bignerdranch.android.testpdfreader.model.word.Word;
import com.bignerdranch.android.testpdfreader.model.word.WordTool;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class TranslationFragmentViewModal extends BaseObservable {
    private IState mState;

    public TranslationFragmentViewModal() {
        reset();
    }

    public void showTranslation(Word word) {
        setState(new TranslateVisibleState(word));
    }

    public void showTranslationError(String message) {
        setState(new TranslateErrorState(message));
    }

    public void reset() {
        setState(new AnimationVisibleState());
    }

    private void setState(IState state) {
        mState = state;
        notifyChange();
    }

    @Bindable
    public String getWord() {
        return mState.getWord();
    }

    @Bindable
    public String getTranscription() {
        return mState.getTranscription();
    }

    @Bindable
    public String getTranslation() {
        return mState.getTranslation();
    }

    @Bindable
    public int getAnimationVisibility() {
        return mState.getAnimationVisibility();
    }

    interface IState {
        String getWord();

        String getTranscription();

        String getTranslation();

        int getAnimationVisibility();
    }

    private class AnimationVisibleState implements IState {

        @Override
        public String getWord() {
            return "";
        }

        @Override
        public String getTranscription() {
            return "";
        }

        @Override
        public String getTranslation() {
            return "";
        }

        @Override
        public int getAnimationVisibility() {
            return View.VISIBLE;
        }
    }

    private class TranslateVisibleState implements IState {
        private Word mWord;

        public TranslateVisibleState(Word word) {
            mWord = word;
        }

        @Override
        public String getWord() {
            return mWord.getWord();
        }

        @Override
        public String getTranscription() {
            if (mWord.getTranscription() != null && mWord.getTranscription().length() > 0) {
                return "[" + mWord.getTranscription() + "]";
            } else {
                return "";
            }
        }

        @Override
        public String getTranslation() {
            WordTool wordTool = new WordTool(mWord);
            return wordTool.translationToStringLine();
        }

        @Override
        public int getAnimationVisibility() {
            return View.GONE;
        }
    }

    private class TranslateErrorState implements IState {

        private String mMessage;

        public TranslateErrorState(String message) {
            mMessage = message;
        }

        @Override
        public String getWord() {
            return mMessage;
        }

        @Override
        public String getTranscription() {
            return "";
        }

        @Override
        public String getTranslation() {
            return "";
        }

        @Override
        public int getAnimationVisibility() {
            return View.GONE;
        }
    }

}
