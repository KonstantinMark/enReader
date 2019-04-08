package com.bignerdranch.android.testpdfreader.view.item;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;


public class PdfMobilePageItem extends BaseObservable {
    private static final String TAG = "ContentItemViewModel";
    private String mContent;
    private IState mState;

    public void setTranslationLoadingAnimationVisibility(boolean flag) {
        setState(flag ? new TranslateLoadingState() : new DefaultState());
    }

    public void showTranslation(String translation) {
        setState(new TranslateVisible(translation));
    }

    public boolean isTranslationVisible() {
        return getState().getVisibilityTranslation() == View.VISIBLE;
    }

    private IState getState() {
        return mState;
    }

    private void setState(IState state) {
        mState = state;
        notifyChange();
    }

    @Bindable
    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
        setState(new DefaultState());
    }

    @Bindable
    public String getTranslation() {
        return getState().getTranslation();
    }

    @Bindable
    public int getVisibilityTranslation() {
        return getState().getVisibilityTranslation();
    }

    @Bindable
    public int getVisibilityTranslateAnimation() {
        return getState().getVisibilityTranslateAnimation();
    }

    @Bindable
    public int getVisibilityTranslateBtn() {
        return getState().getVisibilityTranslateBtn();
    }

    interface IState {
        int getVisibilityTranslateAnimation();

        int getVisibilityTranslation();

        int getVisibilityTranslateBtn();

        String getTranslation();
    }

    private class DefaultState implements IState {

        @Override
        public int getVisibilityTranslateBtn() {
            return View.VISIBLE;
        }

        @Override
        public int getVisibilityTranslateAnimation() {
            return View.GONE;
        }

        @Override
        public int getVisibilityTranslation() {
            return View.GONE;
        }

        @Override
        public String getTranslation() {
            return "";
        }
    }

    private class TranslateLoadingState implements IState {

        @Override
        public int getVisibilityTranslateBtn() {
            return View.GONE;
        }

        @Override
        public int getVisibilityTranslateAnimation() {
            return View.VISIBLE;
        }

        @Override
        public int getVisibilityTranslation() {
            return View.GONE;
        }

        @Override
        public String getTranslation() {
            return "";
        }
    }

    private class TranslateVisible implements IState {

        private String mTranslation;

        public TranslateVisible(String translation) {
            mTranslation = translation;
        }

        @Override
        public int getVisibilityTranslateBtn() {
            return View.VISIBLE;
        }

        @Override
        public int getVisibilityTranslateAnimation() {
            return View.GONE;
        }

        @Override
        public int getVisibilityTranslation() {
            return View.VISIBLE;
        }

        @Override
        public String getTranslation() {
            return mTranslation;
        }
    }
}
