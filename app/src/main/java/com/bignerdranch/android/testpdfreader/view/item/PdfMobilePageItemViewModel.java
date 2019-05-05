package com.bignerdranch.android.testpdfreader.view.item;

import android.view.View;

import com.bignerdranch.android.testpdfreader.model.translator.TranslationListener;
import com.bignerdranch.android.testpdfreader.controller.resource.resource_view.service.impl.PageParagraphWrapper;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;


public class PdfMobilePageItemViewModel extends BaseObservable implements
        TranslationListener {
    private static final String TAG = "ContentItemViewModel";
    private PageParagraphWrapper mContent;
    private IState mState;

    public void setContent(PageParagraphWrapper content) {
        mContent = content;
        setState(new DefaultState());
    }

    public void setTranslationLoadingAnimationVisibility(boolean flag) {
        setState(flag ? new TranslateLoadingState() : new DefaultState());
    }
    public void showTranslation(String translation) {
        setState(new TranslateVisible(translation));
    }

    public boolean isTranslationVisible() {
        return getState().getVisibilityTranslation() == View.VISIBLE;
    }

    @Bindable
    public String getContent() {
        return mContent.content;
    }

    @Bindable
    public int getLabelVisibility() {
        return mContent.isCurrent ? View.VISIBLE : View.GONE;
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

    private IState getState() {
        return mState;
    }

    private void setState(IState state) {
        mState = state;
        notifyChange();
    }

    @Override
    public void onTranslated(String translation) {
        showTranslation(translation);
    }

    @Override
    public void translateError(Error error) {
        showTranslation("Translation error");
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
