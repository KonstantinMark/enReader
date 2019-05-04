package com.bignerdranch.android.testpdfreader.model;

import com.bignerdranch.android.testpdfreader.model.translator.TranslationListener;
import com.bignerdranch.android.testpdfreader.ui.resource.resource_view.page.service.TextSelector;

public class TextTranslationAction {
    public enum ActionType{
        ACTION_WORD_SELECTED, ACTION_PARAGRAPH_SELECTED,
        ACTION_PART_PARAGRAPH_SELECTED, ACTION_UN_SELECT
    }

    private String mText;
    private TextSelector mTextSelector;
    private TranslationListener mTranslationListener;
    private ActionType mActionType;

    public TextTranslationAction(ActionType actionType) {
        mActionType = actionType;
    }

    public ActionType getActionType() {
        return mActionType;
    }

    public String getText() {
        return mText;
    }

    public TextTranslationAction setText(String text) {
        mText = text;
        return this;
    }

    public TextSelector getTextSelector() {
        return mTextSelector;
    }

    public TextTranslationAction setTextSelector(TextSelector textSelector) {
        mTextSelector = textSelector;
        return this;
    }

    public TranslationListener getTranslationListener() {
        return mTranslationListener;
    }

    public TextTranslationAction setTranslationListener(TranslationListener translationListener) {
        mTranslationListener = translationListener;
        return this;
    }
}
